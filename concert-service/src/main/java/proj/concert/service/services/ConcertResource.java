package proj.concert.service.services;

import java.net.URI;
import java.util.*;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.core.*;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.ObjectUtils;
import org.jboss.resteasy.spi.AsynchronousResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import proj.concert.common.dto.*;
import proj.concert.common.types.BookingStatus;
import proj.concert.service.domain.*;
import proj.concert.service.jaxrs.LocalDateTimeParam;
import proj.concert.service.services.Config;
import proj.concert.service.mappers.*;

/*
 * == Concert Resource ==
 * Allows the webapp connects to the database
 */

@Path("/concert-service")
public class ConcertResource {
    private static Logger LOGGER = LoggerFactory.getLogger(ConcertResource.class);
    private static final String WEB_SERVICE_URI = "http://localhost:10000/webservice/services/concert-service";
    private Map<Long, Concert> concertDB = new ConcurrentHashMap<>();
    private AtomicLong idCounter = new AtomicLong();


    /*
    Used to retrieve a specific concert from the database from its id.
     */


    @GET
    @Path("concerts/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveConcert(@PathParam("id") long id){
        EntityManager entityManager = PersistenceManager.instance().createEntityManager();
        try {
            entityManager.getTransaction().begin();
            Concert concert = entityManager.find(Concert.class, id);
            if (concert == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            ConcertDTO concertToDTO = ConcertMapper.toDto(concert);
            return Response.ok(concertToDTO).build();
        } finally{
            entityManager.close();
        }
    }

    /*
    Used to retrive all the concerts from the database and return them as a list of ConcertDTO objects with an OK
     */

    @GET
    @Path("/concerts")
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveConcerts(){
        EntityManager entityManager = PersistenceManager.instance().createEntityManager();
        try {
            entityManager.getTransaction().begin();
            List<Concert> concerts = entityManager.createQuery("SELECT C FROM Concert C", Concert.class).getResultList();
            List<ConcertDTO> concertsToDTO = ConcertMapper.toListDto(concerts);
            GenericEntity<List<ConcertDTO>> entity = new GenericEntity<List<ConcertDTO>>(concertsToDTO) {};
            Response.ResponseBuilder builder = Response.ok(entity);
//            return Response.created(URI.create("/concerts/")).build();
            return Response.ok(entity).build();
//            return Response.created(URI.create("/bookings/" + selectedBooking.getBookingID())).build();
        } finally {
            entityManager.close();
        }
    }

    /*
    Used to retrieve the summary of a concert. This only includes the title and the image name. It is then
    returned as a list of ConcertSummaryDTO objects along with an OK
     */

    @GET
    @Path("/concerts/summaries")
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveConcertSummaries(){
        EntityManager entityManager = PersistenceManager.instance().createEntityManager();
        try {
            entityManager.getTransaction().begin();
            List<Concert> concerts = entityManager.createQuery("SELECT C FROM Concert C", Concert.class).getResultList();
            List<ConcertSummaryDTO> summaries = ConcertMapper.toListSummary(concerts);
            return Response.ok(summaries).build();
        } finally {
            entityManager.close();
        }
    }

    /*
    Used to retrieve a selected performer from the database by passing in the performer id. A PerformerDTO object
    is returned with an OK
     */

    @GET
    @Path("/performers/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrievePerformer(@PathParam("id") long id){
        EntityManager entityManager = PersistenceManager.instance().createEntityManager();
        try {
            entityManager.getTransaction().begin();
            Performer performer = entityManager.find(Performer.class, id);
            if (performer == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            PerformerDTO performerToDTO = PerformerMapper.toDto(performer);
            return Response.ok(performerToDTO).build();
        } finally {
            entityManager.close();
        }
    }

    /*
    Used to retrieve all the performers from the database in a list of PerformerDTO objects with an OK
     */

    @GET
    @Path("/performers")
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrievePerformers(){
     EntityManager entityManager = PersistenceManager.instance().createEntityManager();
        try {
            entityManager.getTransaction().begin();
            List<Performer> performers = entityManager.createQuery("SELECT P FROM Performer P", Performer.class).getResultList();
            List<PerformerDTO> performersToDTO = PerformerMapper.listToDto(performers);
            return Response.ok(performersToDTO).build();
        } finally {
            entityManager.close();
        }
    }

    /*
    Allows the user to login to the service by getting the entered username from the UserDTO object
    and finding the correct user object in the database. We then compare the user object in the table to
    the newly created User object from the .toDomainModel function in our mappper. If they are the same
    a cookie is created and an OK is returned along with the cookie. Otherwise an UNAUTHORIZED is returned.
    */

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response obtainLogin(UserDTO user) {
        EntityManager entityManager = PersistenceManager.instance().createEntityManager();
        try {
            entityManager.getTransaction().begin();
            Query q = entityManager.createQuery("SELECT U FROM User U WHERE U.username = :username", User.class);
            q.setParameter("username", user.getUsername());
            User userDB = (User) q.getSingleResult();
            if (userDB.equals(UserMapper.toDomainModel(user))) {
                NewCookie clientCookie = new NewCookie("auth", Long.toString(userDB.getId()));
                return Response.status(Response.Status.OK).cookie(clientCookie).build();
            }
        } catch(NoResultException e){
            //empty because response is already unauthorized
        } finally{
            entityManager.close();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @POST
    @Path("/bookings")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtainBookings(@CookieParam(Config.CLIENT_COOKIE) Cookie clientId,
                                   BookingRequestDTO bookingRequestDTO) {
        if (clientId == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        Long userID = Long.parseLong(clientId.getValue());
        EntityManager entityManager = PersistenceManager.instance().createEntityManager();

        try {
            entityManager.getTransaction().begin();
            Concert selectedConcert = entityManager.createQuery("SELECT C FROM Concert C WHERE C.id = :id", Concert.class)
                    .setParameter("id", bookingRequestDTO.getConcertId()).getSingleResult();
            if (selectedConcert == null) {
                throw new NoResultException();
            }
            User selectedUser = entityManager.createQuery("SELECT U FROM User U WHERE U.id = :id", User.class)
                    .setParameter("id", userID).getSingleResult();
//            List<Seat> seats = entityManager.createQuery("SELECT S FROM Seat S WHERE S.date = :date", Seat.class)
//                    .setParameter("date", bookingRequestDTO.getDate()).getResultList();

            Booking selectedBooking = new Booking();
            List<Seat> selectedSeats = new ArrayList<Seat>();
            for (String label: bookingRequestDTO.getSeatLabels()){
                Query q = entityManager.createQuery("SELECT S FROM Seat S WHERE S.label = :label AND S.date = :date", Seat.class);
                q.setParameter("label", label);
                q.setParameter("date",bookingRequestDTO.getDate());
                Seat seat = (Seat) q.getSingleResult();
                    if(seat.isBooked()){
                        throw new ForbiddenException();
                    } else {
                        selectedSeats.add(seat);
                        seat.setBooked(true);
                        seat.setBooking(selectedBooking);
                    }
            }

            selectedBooking.setUser(selectedUser);
            selectedBooking.setConcert(selectedConcert);
            selectedBooking.setDate(bookingRequestDTO.getDate());
            selectedBooking.setSeats(selectedSeats);
            entityManager.persist(selectedBooking);
            entityManager.getTransaction().commit();
//            java.net.URI uri = URI.create(WEB_SERVICE_URI + "/bookings/{id}" + selectedBooking.getBookingID());
            System.out.println();
            // IT JUST WONT WORK IF I DONT DO IT LIKE THIS
            Response response = Response.created(URI.create("http://localhost:10000/webservice/services/concert-service/bookings/" + selectedBooking.getBookingID())).build();
            URI uri = response.getLocation();
            return response;
        } catch(NoResultException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch(ForbiddenException e){
            return Response.status(Response.Status.FORBIDDEN).build();
        } catch (Exception e){
            return Response.ok().build();
        }
        finally {
            entityManager.close();
        }
//        return Response.status(Response.Status.FORBIDDEN).build();
    }

    /*
    Finds a booking by its id and returns the BookingDTO with an OK
     */

    @GET
    @Path("/bookings/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveBooking(@PathParam("id") long id, @CookieParam(Config.CLIENT_COOKIE) Cookie clientId) {
        if (clientId == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        Long userID = Long.parseLong(clientId.getValue());
        EntityManager entityManager = PersistenceManager.instance().createEntityManager();

        try {
            entityManager.getTransaction().begin();
            Booking booking = entityManager.createQuery("SELECT B FROM Booking B WHERE B.bookingID = :id", Booking.class)
                    .setParameter("id", id).getSingleResult();
            if (booking == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            } else if (booking.getUser().getId() != userID) { // I need to fix this somehow
                return Response.status(Response.Status.FORBIDDEN).build();
            }
            BookingDTO bookingToDTO = BookingMapper.toDto(booking); //needs to be public before it works
            return Response.ok(bookingToDTO).build();

        } finally {
            entityManager.close();
        }
    }

    @GET
    @Path("/bookings")
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveBookings(@CookieParam(Config.CLIENT_COOKIE) Cookie clientId){
        if (clientId == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        Long userID = Long.parseLong(clientId.getValue());
        EntityManager entityManager = PersistenceManager.instance().createEntityManager();

        try {

            entityManager.getTransaction().begin();
            User user = entityManager.find(User.class, userID);
            List<Booking> bookings = entityManager.createQuery("SELECT B FROM Booking B WHERE B.user = :user", Booking.class)
                    .setParameter("user", user).getResultList();
            List<BookingDTO> bookingsToDTO = BookingMapper.toListDto(bookings); //listToDTO method needs to be made
            return Response.ok(bookingsToDTO).build();
        } finally {
            entityManager.close();
        }
//        return Response.noContent().build();
    }



    @GET
    @Path("seats/{date}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSeats(@PathParam("date") LocalDateTimeParam localDateTime,
                             @QueryParam("status") BookingStatus bookingStatus) {
        if (bookingStatus == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        EntityManager entityManager = PersistenceManager.instance().createEntityManager();
        try {
            entityManager.getTransaction().begin();
            List<Seat> seats = new ArrayList<Seat>();
            if (bookingStatus == bookingStatus.Any) {
                seats = entityManager.createQuery("SELECT s from Seat s WHERE s.date = :date", Seat.class)
                        .setParameter("date", localDateTime.getLocalDateTime()).getResultList();
            } else if (bookingStatus == bookingStatus.Booked) {
                seats = entityManager.createQuery("SELECT s from Seat s WHERE s.date = :date AND s.isBooked = true", Seat.class)
                        .setParameter("date", localDateTime.getLocalDateTime()).getResultList();
            } else {
                seats = entityManager.createQuery("SELECT s from Seat s WHERE s.date = :date AND s.isBooked = false", Seat.class)
                        .setParameter("date", localDateTime.getLocalDateTime()).getResultList();
            }
            List<SeatDTO> seatDTOs = SeatMapper.toListDto(seats);
            return Response.ok(seatDTOs).build();
        } finally {
            entityManager.close();
        }
    }

/*
    @POST
    @Path("/subscribe/concertInfo")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtainSubscription(@CookieParam(Config.CLIENT_COOKIE) Cookie clientId,
                                       ConcertInfoSubscriptionDTO concertSubscription,
                                       AsyncResponse async) {
        if (clientId == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        Long userID = Long.parseLong(clientId.getValue());
        EntityManager entityManager = PersistenceManager.instance().createEntityManager();
        try {
            entityManager.getTransaction().begin();
            Concert concert = entityManager.find(Concert.class, concertSubscription.getConcertId());
            if (concert == null) {
                async.resume(Response.status(Response.Status.BAD_REQUEST).build());
                return;
            }
            List<Seat> seats = entityManager.createQuery("SELECT s from Seat s WHERE s.date = :date", Seat.class)
                    .setParameter("date", concertSubscription.getDate()).getResultList();
            int seatCounter = 0;
            for (Seat s:seats) {
                if (s.isBooked()) {
                    seatCounter++;
                }
            }
            if (seatCounter/120*100 <= concertSubscription.getPercentageBooked()) {
                ConcertInfoNotificationDTO notificationDTO = new ConcertInfoNotificationDTO(120-seatCounter);
                return Response.ok(notificationDTO).build();
            }
        } finally {
            entityManager.close();
        }
        return Response.status(Response.Status.BAD_REQUEST).build(); //Might break our code lmao
    }
    //I need this when we need to use auth
    //@CookieParam(Config.CLIENT_COOKIE) Cookie clientId

 */
}
