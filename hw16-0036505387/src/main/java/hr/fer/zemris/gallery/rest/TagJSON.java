package hr.fer.zemris.gallery.rest;

import hr.fer.zemris.gallery.model.GalleryProvider;
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

@Path("/tag")
public class TagJSON {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTagsList() {
		Set<String> tags = GalleryProvider.getInstance().getTags();
		JSONArray json = new JSONArray();

		tags.forEach(json::put);

		return Response.ok(json.toString()).build();
	}

	@Path("{tag}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getListOfImagesForTag(@PathParam("tag") String tag) {
		List<Image> images = GalleryProvider.getInstance().getImagesForTag(tag);
		JSONArray json = new JSONArray();

		if (images != null && images.size() != 0) {
			images.forEach(img -> json.put(img.getFileName()));
		}

		return Response.ok(json.toString()).build();
	}

}
