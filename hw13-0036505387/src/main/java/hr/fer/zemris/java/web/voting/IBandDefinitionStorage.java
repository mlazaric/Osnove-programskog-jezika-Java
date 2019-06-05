package hr.fer.zemris.java.web.voting;

public interface IBandDefinitionStorage extends Iterable<Band> {

    Band getById(int id);

}
