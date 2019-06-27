package hr.fer.zemris.gallery.rest;

import hr.fer.zemris.gallery.model.GalleryProvider;
import hr.fer.zemris.gallery.model.Image;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Set;

@Path("/image")
public class ImageJSON {

	@Path("{image}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getInformationForImage(@PathParam("image") String name) {
		Image image = GalleryProvider.getInstance().getImageFromName(name);

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
