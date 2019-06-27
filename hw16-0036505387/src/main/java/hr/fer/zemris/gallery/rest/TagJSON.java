package hr.fer.zemris.gallery.rest;

import hr.fer.zemris.gallery.model.GalleryStorageProvider;
import hr.fer.zemris.gallery.model.Image;
import org.json.JSONArray;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Set;

/**
 * Provides a REST API for interacting with tags.
 *
 * @author Marko Lazarić
 */
@Path("/tag")
public class TagJSON {

	/**
	 * Returns a JSON array of tag names which are currently used in the {@link hr.fer.zemris.gallery.model.GalleryStorage}.
	 *
	 * @return a JSON array of tag names which are currently used
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTagsList() {
		Set<String> tags = GalleryStorageProvider.getInstance().getTags();
		JSONArray json = new JSONArray();

		tags.forEach(json::put);

		return Response.ok(json.toString()).build();
	}

	/**
	 * Returns a JSON array of image file names which are tagged with the specified tag.
	 *
	 * @param tag the tag whose images are being queried
	 * @return a JSON array of image file names which are tagged with the specified tag
	 */
	@Path("{tag}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getListOfImagesForTag(@PathParam("tag") String tag) {
		List<Image> images = GalleryStorageProvider.getInstance().getImagesForTag(tag);
		JSONArray json = new JSONArray();

		if (images != null && images.size() != 0) {
			images.forEach(img -> json.put(img.getFileName()));
		}

		return Response.ok(json.toString()).build();
	}

}
