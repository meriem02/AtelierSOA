package webservices;

import entities.RendezVous;
import metiers.RendezVousBusiness;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/rendezvous")
public class RendezVousRessources {

     static RendezVousBusiness rendezVousBusiness = new RendezVousBusiness();

    // GET - récupérer tous les rendez-vous
    @GET
    @Path("/getAll")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        try {
            List<RendezVous> liste = rendezVousBusiness.getListeRendezVous();
            return Response.status(200)
                    .header("Access-Control-Allow-Origin", "*")
                    .entity(liste)
                    .build();
        } catch (Exception e) {
            return Response.status(500)
                    .header("Access-Control-Allow-Origin", "*")
                    .entity("Erreur lors de la récupération des rendez-vous")
                    .build();
        }
    }

    // GET - récupérer un rendez-vous par id
    @GET
    @Path("/getById/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") int id) {
        try {
            RendezVous rdv = rendezVousBusiness.getRendezVousById(id);
            if (rdv != null) {
                return Response.status(200)
                        .header("Access-Control-Allow-Origin", "*")
                        .entity(rdv)
                        .build();
            } else {
                return Response.status(404)
                        .header("Access-Control-Allow-Origin", "*")
                        .entity("Rendez-vous avec id " + id + " non trouvé")
                        .build();
            }
        } catch (Exception e) {
            return Response.status(500)
                    .header("Access-Control-Allow-Origin", "*")
                    .entity("Erreur lors de la récupération du rendez-vous")
                    .build();
        }
    }

    // GET - récupérer les rendez-vous par référence logement
    @GET
    @Path("/getByLogementReference/{reference}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByLogementReference(@PathParam("reference") int reference) {
        try {
            List<RendezVous> liste = rendezVousBusiness.getListeRendezVousByLogementReference(reference);
            return Response.status(200)
                    .header("Access-Control-Allow-Origin", "*")
                    .entity(liste)
                    .build();
        } catch (Exception e) {
            return Response.status(500)
                    .header("Access-Control-Allow-Origin", "*")
                    .entity("Erreur lors de la récupération des rendez-vous par référence logement")
                    .build();
        }
    }

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addRendezVous(RendezVous rendezVous) {
        try {
            if (rendezVous == null) {
                return Response.status(400)
                        .header("Access-Control-Allow-Origin", "*")
                        .entity("{\"error\": \"Données du rendez-vous invalides\"}")
                        .build();
            }

            boolean created = rendezVousBusiness.addRendezVous(rendezVous);
            if (created) {
                return Response.status(201)
                        .header("Access-Control-Allow-Origin", "*")
                        .entity("{\"message\": \"Rendez-vous ajouté avec succès\", \"id\": " + rendezVous.getId() + "}")
                        .build();
            } else {
                return Response.status(409)
                        .header("Access-Control-Allow-Origin", "*")
                        .entity("{\"error\": \"Erreur lors de l'ajout du rendez-vous\"}")
                        .build();
            }
        } catch (Exception e) {
            return Response.status(500)
                    .header("Access-Control-Allow-Origin", "*")
                    .entity("{\"error\": \"Erreur interne: " + e.getMessage() + "\"}")
                    .build();
        }
    }

    @PUT
    @Path("/update/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateRendezVous(@PathParam("id") int id, RendezVous updatedRendezVous) {
        try {
            if (updatedRendezVous == null) {
                return Response.status(400)
                        .header("Access-Control-Allow-Origin", "*")
                        .entity("{\"error\": \"Données du rendez-vous invalides\"}")
                        .build();
            }

            boolean updated = rendezVousBusiness.updateRendezVous(id, updatedRendezVous);
            if (updated) {
                return Response.status(200)
                        .header("Access-Control-Allow-Origin", "*")
                        .entity("{\"message\": \"Rendez-vous mis à jour avec succès\"}")
                        .build();
            } else {
                return Response.status(404)
                        .header("Access-Control-Allow-Origin", "*")
                        .entity("{\"error\": \"Rendez-vous non trouvé\"}")
                        .build();
            }
        } catch (Exception e) {
            return Response.status(500)
                    .header("Access-Control-Allow-Origin", "*")
                    .entity("{\"error\": \"Erreur lors de la mise à jour\"}")
                    .build();
        }
    }

    @DELETE
    @Path("/delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteRendezVous(@PathParam("id") int id) {
        try {
            boolean deleted = rendezVousBusiness.deleteRendezVous(id);
            if (deleted) {
                return Response.status(200)
                        .header("Access-Control-Allow-Origin", "*")
                        .entity("{\"message\": \"Rendez-vous supprimé avec succès\"}")
                        .build();
            } else {
                return Response.status(404)
                        .header("Access-Control-Allow-Origin", "*")
                        .entity("{\"error\": \"Rendez-vous non trouvé\"}")
                        .build();
            }
        } catch (Exception e) {
            return Response.status(500)
                    .header("Access-Control-Allow-Origin", "*")
                    .entity("{\"error\": \"Erreur lors de la suppression\"}")
                    .build();
        }
    }

    // OPTIONS - gérer les requêtes CORS preflight
    @OPTIONS
    @Path("/{path:.*}")
    public Response handleCORS() {
        return Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
                .header("Access-Control-Allow-Headers", "Content-Type, Authorization")
                .build();
    }
}
