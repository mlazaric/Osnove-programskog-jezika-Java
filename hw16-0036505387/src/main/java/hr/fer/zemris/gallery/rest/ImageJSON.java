package hr.fer.zemris.gallery.rest;

import hr.fer.zemris.gallery.model.GalleryStorageProvider;
import hr.fer.zemris.gallery.model.Image;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Provides a REST API for interacting with {@link Image}.
 *
 * @author Marko Lazarić
 */
@Path("/image")
public class ImageJSON {

	/**
	 * If the selected image does not exist within {@link hr.fer.zemris.gallery.model.GalleryStorage}, it returns a
	 * NOT_FOUND response status.
	 *
	 * Otherwise it returns a JSON response containing the file name, description and the list of tags for the image
	 * specified in the URL.
	 *
	 * @param name the file name of the image whose information is being queried
	 * @return a NOT_FOUND response status if the image does not exist or a JSON response containing information about
	 *         the image
	 */
	@Path("{image}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getInformationForImage(@PathParam("image") String name) {
		Image image = GalleryStorageProvider.getInstance().getImageFromName(name);

		if (image == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}

		JSONObject json = new JSONObject();

		json.put("name", image.getFileName());
		json.put("description", image.getDescription());
		json.put("tags", new JSONArray(image.getTags()));

		return Response.ok(json.toString()).build();
	}

}
