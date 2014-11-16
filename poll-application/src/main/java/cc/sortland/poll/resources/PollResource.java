package cc.sortland.poll.resources;

import cc.sortland.poll.Id;
import cc.sortland.poll.Link;
import cc.sortland.poll.Poll;
import cc.sortland.poll.PollManager;
import cc.sortland.poll.Vote;
import com.codahale.metrics.annotation.Timed;
import com.google.common.collect.ImmutableList;
import java.net.URI;
import java.util.Optional;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Produces(MediaType.APPLICATION_JSON)
@Path("polls")
public class PollResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(PollResource.class);

    private final PollManager pollManager;

    public PollResource(final PollManager pollManager) {
        this.pollManager = pollManager;
    }

    @Timed
    @POST
    public Response postPoll(@Context UriInfo uriInfo, final Poll poll) {
        LOGGER.info("Received request: {}", poll);
        final Poll createdPoll = pollManager.addPoll(poll);
        final URI location
                = uriInfo.getAbsolutePathBuilder().path(createdPoll.id().getStringValue()).build();
        final Poll enrichedPoll = enrichPollLink(createdPoll, location);
        return Response.created(location).entity(enrichedPoll).build();
    }

    private Poll enrichPollLink(Poll createdPoll, URI location) {
        return Poll.createBuilder(createdPoll).links(ImmutableList.of(
                Link.create("self", "get", location),
                Link.create("delete", "delete", location),
                Link.create("create vote", "post", location)
        )).build();
    }

    @Timed
    @GET
    @Path("{pollId}")
    public Response getPoll(@Context UriInfo uriInfo, @PathParam("pollId") final Id id) {
        final Optional<Poll> poll = pollManager.getPoll(id);
        if (!poll.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        final URI location
                = uriInfo.getAbsolutePathBuilder().path(poll.get().id().getStringValue()).build();
        final Poll enrichedPoll = enrichPollLink(poll.get(), location);
        return Response.ok().entity(enrichedPoll).build();
    }

    @Timed
    @POST
    @Path("{pollId}")
    public Response postVote(
            @Context final UriInfo uriInfo,
            @PathParam("pollId") final Id id,
            final Vote vote) {
        final Optional<Vote> optionalAddedVote = pollManager.addVote(id, vote);
        if (!optionalAddedVote.isPresent()) {
            return Response.serverError().build();
        }
        final Vote addedVote = optionalAddedVote.get();
        final URI location = uriInfo
                .getAbsolutePathBuilder()
                .path("votes")
                .path(addedVote.id().getStringValue())
                .build();
        return Response.created(location).entity(addedVote).build();
    }

    @Timed
    @DELETE
    @Path("{pollId}")
    public Response deleteVote(
            @Context final UriInfo uriInfo,
            @PathParam("pollId") final Id id,
            final Vote vote) {
        final boolean success = pollManager.deletePoll(id);
        if (!success) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().build();
    }

    @Timed
    @GET
    @Path("{pollId}/votes/{id}")
    public Response getVote(
            @PathParam("pollId") final Id id,
            @PathParam("id") final Id voteId) {
        final Optional<Vote> vote = pollManager.getVote(id, voteId);
        if (!vote.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok().entity(vote.get()).build();
    }

    @Timed
    @DELETE
    @Path("{pollId}/votes/{id}")
    public Response deleteVote(
            @PathParam("pollId") final Id id,
            @PathParam("id") final Id voteId) {
        final Optional<Vote> vote = pollManager.deleteVote(id, voteId);
        if (!vote.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().build();
    }
}
