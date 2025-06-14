package webservices;

import entities.Logement;
import metiers.LogementBusiness;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/logement")
public class LogementRessources {
   static LogementBusiness help = new LogementBusiness();

    // GET - Récupérer tous les logements
    @GET
    @Path("/getAll")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        try {
            List<Logement> logements = help.getLogements();
            return Response.status(200)
                    .header("Access-Control-Allow-Origin", "*")
                    .entity(logements)
                    .build();
        } catch (Exception e) {
            return Response.status(500)
                    .header("Access-Control-Allow-Origin", "*")
                    .entity("Erreur lors de la récupération des logements")
                    .build();
        }
    }

    // GET - Récupérer un logement par référence
    @GET
    @Path("/getByReference/{reference}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByReference(@PathParam("reference") int reference) {
        try {
            Logement logement = help.getLogementsByReference(reference);
            if (logement != null) {
                return Response.status(200)
                        .header("Access-Control-Allow-Origin", "*")
                        .entity(logement)
                        .build();
            } else {
                return Response.status(404)
                        .header("Access-Control-Allow-Origin", "*")
                        .entity("Logement avec référence " + reference + " non trouvé")
                        .build();
            }
        } catch (Exception e) {
            return Response.status(500)
                    .header("Access-Control-Allow-Origin", "*")
                    .entity("Erreur lors de la récupération du logement")
                    .build();
        }
    }

    // GET - Récupérer les logements par délégation
    @GET
    @Path("/getByDelegation/{delegation}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByDelegation(@PathParam("delegation") String delegation) {
        try {
            List<Logement> logements = help.getLogementsByDeleguation(delegation);
            return Response.status(200)
                    .header("Access-Control-Allow-Origin", "*")
                    .entity(logements)
                    .build();
        } catch (Exception e) {
            return Response.status(500)
                    .header("Access-Control-Allow-Origin", "*")
                    .entity("Erreur lors de la récupération des logements par délégation")
                    .build();
        }
    }

    // POST - Ajouter un nouveau logement
    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addLogement(Logement logement) {
        try {
            if (logement == null) {
                return Response.status(400)
                        .header("Access-Control-Allow-Origin", "*")
                        .entity("{\"error\":\"Données du logement invalides\"}")
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            Logement existant = help.getLogementsByReference(logement.getReference());
            if (existant != null) {
                return Response.status(409)
                        .header("Access-Control-Allow-Origin", "*")
                        .entity("{\"error\":\"Un logement avec cette référence existe déjà\"}")
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            boolean success = help.addLogement(logement);
            if (success) {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Logement ajouté avec succès");
                response.put("reference", logement.getReference());

                return Response.status(201)
                        .header("Access-Control-Allow-Origin", "*")
                        .entity(response)
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            } else {
                return Response.status(500)
                        .header("Access-Control-Allow-Origin", "*")
                        .entity("{\"error\":\"Erreur lors de l'ajout du logement\"}")
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }
        } catch (Exception e) {
            return Response.status(500)
                    .header("Access-Control-Allow-Origin", "*")
                    .entity("{\"error\":\"Erreur interne: " + e.getMessage() + "\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }

    // Méthode helper pour parser le JSON manuellement (pour debug)
    private Logement parseLogementFromJson(String json) {
        try {
            // Parse simple du JSON - remplacez par votre parser habituel si nécessaire
            json = json.trim().replaceAll("[{}]", "");
            String[] pairs = json.split(",");

            Logement logement = new Logement();

            for (String pair : pairs) {
                String[] keyValue = pair.split(":");
                if (keyValue.length == 2) {
                    String key = keyValue[0].trim().replaceAll("\"", "");
                    String value = keyValue[1].trim().replaceAll("\"", "");

                    switch (key) {
                        case "reference":
                            logement.setReference(Integer.parseInt(value));
                            break;
                        case "adresse":
                            logement.setAdresse(value);
                            break;
                        case "delegation":
                            logement.setDelegation(value);
                            break;
                        case "gouvernorat":
                            logement.setGouvernorat(value);
                            break;
                        case "type":
                            logement.setType(value);
                            break;
                        case "description":
                            logement.setDescription(value);
                            break;
                        case "prix":
                            logement.setPrix(Float.parseFloat(value));
                            break;
                    }
                }
            }
            return logement;
        } catch (Exception e) {
            System.out.println("Erreur parsing JSON: " + e.getMessage());
            return null;
        }
    }

    // PUT - Modifier un logement existant
    @PUT
    @Path("/update/{reference}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateLogement(@PathParam("reference") int reference, Logement logement) {
        try {
            if (logement == null) {
                return Response.status(400)
                        .header("Access-Control-Allow-Origin", "*")
                        .entity("{\"error\":\"Données du logement invalides\"}")
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            logement.setReference(reference);

            boolean success = help.updateLogement(reference, logement);
            if (success) {
                // Réponse JSON
                Map<String, String> response = new HashMap<>();
                response.put("message", "Logement mis à jour avec succès");

                return Response.status(200)
                        .header("Access-Control-Allow-Origin", "*")
                        .entity(response)
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            } else {
                return Response.status(404)
                        .header("Access-Control-Allow-Origin", "*")
                        .entity("{\"error\":\"Logement non trouvé\"}")
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }
        } catch (Exception e) {
            return Response.status(500)
                    .header("Access-Control-Allow-Origin", "*")
                    .entity("{\"error\":\"Erreur lors de la mise à jour\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }


    @DELETE
    @Path("/delete/{reference}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteLogement(@PathParam("reference") int reference) {
        try {
            boolean success = help.deleteLogement(reference);
            if (success) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Logement supprimé avec succès");

                return Response.status(200)
                        .header("Access-Control-Allow-Origin", "*")
                        .entity(response)
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            } else {
                return Response.status(404)
                        .header("Access-Control-Allow-Origin", "*")
                        .entity("{\"error\":\"Logement non trouvé\"}")
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }
        } catch (Exception e) {
            return Response.status(500)
                    .header("Access-Control-Allow-Origin", "*")
                    .entity("{\"error\":\"Erreur lors de la suppression\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }



    // OPTIONS - Pour gérer les requêtes CORS preflight
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