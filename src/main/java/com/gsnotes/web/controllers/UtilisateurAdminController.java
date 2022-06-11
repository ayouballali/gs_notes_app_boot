package com.gsnotes.web.controllers;

import com.gsnotes.bo.*;
import com.gsnotes.dao.IEudiant;
import com.gsnotes.dao.IInscriptionAnnuelleDoa;
import com.gsnotes.dao.IniveauDoa;
import com.gsnotes.exelHandler.ExcelHandler;
import com.gsnotes.exelHandler.FileManagerHelper;
import com.gsnotes.services.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.gsnotes.utils.export.ExcelExporter;
import com.gsnotes.web.models.PersonModel;
import com.gsnotes.web.models.UserAndAccountInfos;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import javax.validation.Valid;

/**
 * Ce controleur gère les personnes de type Etudiant, Enseignant et Cadre Admin
 * 
 * @author Boudaa
 *
 */

@Controller
@RequestMapping("/admin")
public class UtilisateurAdminController {

	@Autowired
	private IPersonService personService;

	@Autowired
	private IniveauService niveauService ;

	@Autowired
	private IEtudiantService etudiantService;
	private final List<String> CONTENT_TYPES = Arrays.asList("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "application/vnd.ms-excel");


	@Autowired
	private IInscriptionService inscriptionService;

	@Autowired
	private HttpSession httpSession;
	
	
	/** Utilisé pour la journalisation */
	private Logger LOGGER = LoggerFactory.getLogger(getClass());


	public UtilisateurAdminController() {



	}

	@RequestMapping("admin/processMod")
	public String proceMod(RedirectAttributes redirectAttributes){
		redirectAttributes.addFlashAttribute("message","mise a jour a ete bien fait " );
		return "redirect:/admin/showAdminHome";
	}


//@RequestMapping("/showAdminHome");
//	@RequestMapping("/processFileexel"){
@PostMapping("/upload") // //new annotation since 4.3
public String processModulesNotes(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {



	if (!isValid(file)){
		redirectAttributes.addFlashAttribute("message", "the file is either empty or the not an excel file");
		return "redirect:/admin/showAdminHome";
	}
// creer  list d etudiant que je dois
	ArrayList<Etudiant> upoadedEtd = new ArrayList<>();


	ArrayList<Etudiant> modifidEtd = new ArrayList<>();

	ArrayList<Etudiant> modifidEtdexl = new ArrayList<>();




	String TEMP_FOLDER = FileManagerHelper.getAbsolutePathProject() + "/TEMP/";

	try {
		byte[] bytes = file.getBytes();
		Path dir = Paths.get(TEMP_FOLDER);
		Path path = Paths.get(TEMP_FOLDER + file.getOriginalFilename());

		if(!Files.exists(dir)) {
			Files.createDirectories(dir);
		}

		Files.write(path, bytes);

		redirectAttributes.addFlashAttribute("message", "You successfully uploaded : " + file.getOriginalFilename());


	} catch (IOException e) {
		redirectAttributes.addFlashAttribute("message", "Server throw IOException");
		e.printStackTrace();
	}
	//	// get the data from the excel file

			try {
				List<ArrayList<Object>> data = ExcelHandler.readFromExcel(TEMP_FOLDER + file.getOriginalFilename(), 0);

				int i=0 ;
				for(ArrayList<Object> row : data) {
					if (i==0){i++;continue;}
					System.out.println("hi boa");
                   // get each ligne in th file
					Long idEtd = ((Double) row.get(0)).longValue();
					System.out.println("hi boa2");
				   Long idniveau = ((Double) row.get(4)).longValue();
				   String cneEtd = (String) row.get(1);
				   String nomEtd = (String) row.get(2);
				   String prenomEtd = (String) row.get(3);
					Etudiant etudiantExl = new Etudiant();
					etudiantExl.setCne(cneEtd);
					etudiantExl.setNom(nomEtd);
					etudiantExl.setPrenom(prenomEtd);

				   String type=(String) row.get(5);
				   // creer object etudiant pour stocker les donnees de la base de donne
					Etudiant etudiant = etudiantService.getEtudiantById(idEtd)!=null? (Etudiant) etudiantService.getEtudiantById(idEtd) :null;

					// cree une  inscription
					InscriptionAnnuelle inscriptionAnnuelle = new InscriptionAnnuelle();
                    Niveau niveau;


					// first verifier est ce que le idniveau est valide
                    // recuperer le niveau
                    // verifier est ce que l'identifiant existe
					if(verifIdenNiveau(idniveau)){
						// si il est
                         niveau = niveauService.getById(idniveau);

					}else {
						// si l'identifiant n'est pas dans la bse de donnee
						// stocker l'erreur exactement
						System.out.println("id niveau est "+idniveau);
                        // return dans le jsp avec une erreur
                        redirectAttributes.addFlashAttribute("message2", "l'etudiant avec  "+"id="+idEtd+"a un identifiant de  niveau qui " +
                                "  n'existe pas " + "dans la base de donnee  ");
                        return "redirect:/admin/showAdminHome";
					}
					// verifier inscription et reinscription
					if(type.equals("Réinscription")) {
						System.out.println("c'est une rinscription ");
						if (etudiant != null) {
							// c'est bien
                            // completer la verification
							// verifier est ce que le niveau ne contaradis pas avec inscription annuelle
							System.out.println("i am not null ");
							if(contarductionNotes(etudiant,idniveau)){
								System.out.println("proble dans cintraduction----------------- ");
                         		// verifier les donnee est ce sont semblabele ou il faut les mise a jourer
								if (sameinfo(etudiant,etudiantExl)){
									// alors continue et
									// cree inscription


								}else {// afficher une page pour selectioner le etudiants que je dois faire un mise a jour
									System.out.println("des donnees difirentes ");
									modifidEtd.add(etudiant);
									modifidEtdexl.add(etudiantExl);
								}

								}else{
								// ecrire l'erreur
								redirectAttributes.addFlashAttribute("message2", "Le niveau indiqué dans le fichier pour  étudiant"+ "id="+idEtd+ "est "  +
										"contradictoire avec ses résultats de l’année passée ou avec ses inscriptions \n" +
										"précédentes " );
								return "redirect:/admin/showAdminHome";
								}







						}else {
							// generer une eror que cette personne doit etre dans la base de donnee
							// return dans le jsp avec une erreur
							redirectAttributes.addFlashAttribute("message2", "l'etudiant avec l' "+"id="+idEtd+"demande une reinscription mais n'existe pas " +
																	"dans la base de donnee  ");
							return "redirect:/admin/showAdminHome";

						}
					}else {System.out.println("c'est une inscription----------------- ");

						// il s'agit d'une inscription
						if (etudiant == null) {
							// c'est bien alors
                            // verifier dans l'exele est ce que les donnes compatible (idLevel ===2)
							if(idniveau ==2){
								// etudiant a le bon niveau
								// save this etudiant in table etudiant et table person










							}else {
								// l'etudiant a un mauvais idniveau ,generer une erreur
								redirectAttributes.addFlashAttribute("message2", "l'etudiant avec  "+"id="+idEtd+"a un idNiveau pas valide  " );
								return "redirect:/admin/showAdminHome";
							}


						}else {

                            // generer une eror que cette personne ne  doit pas  etre dans la base de donnee
                            redirectAttributes.addFlashAttribute("message2", "l'etudiant avec  "+"id="+idEtd+"demande une inscription mais il est deja " +
                                  "dans la base de donnee  ");
							return "redirect:/admin/showAdminHome";

						}
					}










									}



int k=0;
				// boucle 2 for save data
				for(ArrayList<Object> row : data) {
					if (k==0){k++;continue;}
					// get each ligne in th file
					Long idEtd = ((Double) row.get(0)).longValue();
					Long idniveau = ((Double) row.get(4)).longValue();
					String cneEtd = (String) row.get(1);
					String nomEtd = (String) row.get(2);
					String prenomEtd = (String) row.get(3);
					Etudiant etudiantExl = new Etudiant();
					etudiantExl.setCne(cneEtd);
					etudiantExl.setNom(nomEtd);
					etudiantExl.setPrenom(prenomEtd);

					String type=(String) row.get(5);
					// creer object etudiant pour stocker les donnees de la base de donne
					Etudiant etudiant = etudiantService.getEtudiantById(idEtd)!=null? (Etudiant) etudiantService.getEtudiantById(idEtd) :null;

					// cree une  inscription
					InscriptionAnnuelle inscriptionAnnuelle = new InscriptionAnnuelle();
					Niveau niveau;


					// first verifier est ce que le idniveau est valide
					// recuperer le niveau
					// verifier est ce que l'identifiant existe

						niveau = niveauService.getById(idniveau);


					// verifier inscription et reinscription
					if(type.equals("Réinscription")) {
						System.out.println("c'est une rinscription----------------- ");

							// c'est bien
							// completer la verification
							// verifier est ce que le niveau ne contaradis pas avec inscription annuelle

								// verifier les donnee est ce sont semblabele ou il faut les mise a jourer
								if (sameinfo(etudiant,etudiantExl)){

									// alors continue et
									// cree inscription
									inscriptionAnnuelle.setEtudiant(etudiant);

									inscriptionAnnuelle.setNiveau(niveau);

									// aussi save une inscription
									inscriptionService.addInscription(inscriptionAnnuelle);
									System.out.println("inscription est "+inscriptionAnnuelle);
									// affecter les modules a l'inscription modules
									InscriptionModule inscriptionModule = new InscriptionModule();

									for (int j=0;j<niveau.getModules().size();j++){

										inscriptionModule.setModule(niveau.getModules().get(j));
										inscriptionModule.setInscriptionAnnuelle(inscriptionAnnuelle);
										inscriptionService.addInscriptionModuleDoa(inscriptionModule);

									}
								}else {// afficher une page pour selectioner le etudiants que je dois faire un mise a jour
									System.out.println("des donnees difirentes ");
									continue;

								}










					}else {System.out.println("c'est une inscription----------------- ");

						// il s'agit d'une inscription

							// c'est bien alors
							// verifier dans l'exele est ce que les donnes compatible (idLevel ===2)

								// etudiant a le bon niveau
								// save this etudiant in table etudiant et table person


								// ajouter cet etudiant a la base de donnee
								upoadedEtd.add(etudiantExl);
								etudiantService.addEtudiant(etudiantExl);
								personService.addPerson((Utilisateur) etudiantExl);
								inscriptionAnnuelle.setAnnee((new Date()).getYear()+1900);

								inscriptionAnnuelle.setEtudiant(etudiantExl);

								inscriptionAnnuelle.setNiveau(niveau);

								// aussi save une inscription
								inscriptionService.addInscription(inscriptionAnnuelle);

								// affecter les modules a l'inscription modules
								InscriptionModule inscriptionModule = new InscriptionModule();

								for (int j=0;j<niveau.getModules().size();j++){

									inscriptionModule.setModule(niveau.getModules().get(j));
									inscriptionModule.setInscriptionAnnuelle(inscriptionAnnuelle);
									inscriptionService.addInscriptionModuleDoa(inscriptionModule);

								}







					}



				}
				System.out.println("modifidEtd   ------------------------------"+modifidEtdexl);
if (modifidEtd.size()!=0){
	redirectAttributes.addFlashAttribute("listEtd", modifidEtd);
	redirectAttributes.addFlashAttribute("cont", modifidEtd.size());
	redirectAttributes.addFlashAttribute("listEtdexl", modifidEtdexl);
	// afficher le view
	return "redirect:/admin/showAdminHomeMod";
}



				//  afaire .....................................


				// traiter les gens qui ont ajourne
				// traiter le tyoe du file
				// traiter les types de donnee
				// traiter lorsque on a des donnee contraductoire // si il y a des donne different on donne la possiblite de les mise a jour
				// traiter les problems de du duplication

			} catch (Exception e) {
				System.out.println("read from excel exception " + e.getMessage());

			}




	return "admin/adminHome";
}


private void deleteEtd(ArrayList<Long> id){
		for (Long i : id){
			etudiantService.deleteEtudiant(i);
		}
}

	private void deleteinscrAnn(ArrayList<Long> id){
		for (Long i : id){
			inscriptionService.deleteinsAnul(i);
		}
	}

	private void deleteinscMOd(ArrayList<Long> id){
		for (Long i : id){
			inscriptionService.deleteinsMod(i);
		}
	}

private boolean sameinfo(Etudiant et,Etudiant etExl){

		if ( et.getCne().equals(etExl.getCne()) &&  et.getNom().equals(etExl.getNom()) && et.getPrenom().equals( etExl.getPrenom() )){
		return  true;
		}

		return  false;
}



private boolean contarductionNotes(Etudiant etudiant ,Long idNiv){

		List<InscriptionAnnuelle> inscriptionAnnuelles = etudiant.getInscriptions();


	InscriptionAnnuelle inscriptionAnnuelle = Collections.max(inscriptionAnnuelles, Comparator.comparing(c -> c.getAnnee()));;
	System.out.println("proble dans cintraduction1----------------- ");
	if (inscriptionAnnuelle.getNiveau().getIdNivSuivant() == idNiv && inscriptionAnnuelle.getValidation().equals("valide")){
		// c'est bien\
		System.out.println("proble dans cintraduction1.1----------------- ");
		return true;
	}
	System.out.println("proble dans cintraduction1.2----------------- ");
	return false;
}




	//pour id niveau
private  boolean verifIdenNiveau(Long id){
		return niveauService.existsById(id);
}




	// verify the type of the file
	private boolean isValid(MultipartFile file) {
		System.out.println("============================");
		System.out.println("content type = " + file.getContentType());
		System.out.println("original name = " + file.getOriginalFilename());
		System.out.println("size = " + file.getSize());
		System.out.println("resource = " + file.getResource());
		System.out.println("============================");


		if (file.isEmpty() || !CONTENT_TYPES.contains(file.getContentType())) {
			return false;
		}

		return true;
	}



	@RequestMapping(value = "showForm", method = RequestMethod.GET)
	public String showForm(@RequestParam int typePerson, Model model) {

		PersonModel pmodel = new PersonModel(typePerson);
		model.addAttribute("personModel", pmodel);

		// Nous avons choisit d'utiliser une classe modèle personnalisée
		// définie par PersonModel pour une meilleur flexibilité

		List<Utilisateur> persons = personService.getAllPersons();
		List<PersonModel> modelPersons = new ArrayList<PersonModel>();
		// On copie les données des personnes vers le modèle
		for (int i = 0; i < persons.size(); i++) {
			PersonModel pm = new PersonModel();
			if (persons.get(i) instanceof Etudiant) {
				// permet de copier les données d'un objet à l'autre à codition
				// d'avoir les meme attributs (getters/setters)
				BeanUtils.copyProperties((Etudiant) persons.get(i), pm);
				// On fixe le type (cet attribut ne sera pas copié automatiquement)
				pm.setTypePerson(PersonModel.TYPE_STUDENT);

				// Mettre la personne dans le modèle
				modelPersons.add(pm);
			} else if (persons.get(i) instanceof Enseignant) {

				BeanUtils.copyProperties((Enseignant) persons.get(i), pm);
				pm.setTypePerson(PersonModel.TYPE_PROF);
				modelPersons.add(pm);
			} else if (persons.get(i) instanceof CadreAdministrateur) {
				BeanUtils.copyProperties((CadreAdministrateur) persons.get(i), pm);
				pm.setTypePerson(PersonModel.TYPE_CADRE_ADMIN);
				modelPersons.add(pm);
			}
		}

		// Mettre la liste des personnes dans le modèle de Spring MVC
		model.addAttribute("personList", modelPersons);

		return "admin/form";
	}

	@RequestMapping(value = "addPerson", method = RequestMethod.POST)
	public String process(@Valid @ModelAttribute("personModel") PersonModel person, BindingResult bindingResult,
			Model model, HttpServletRequest rq) {

		// En cas d'erreur de validation
		if (bindingResult.hasErrors()) {
			// rq.setAttribute("typePerson", +person.getTypePerson());
			return "admin/form";
		}

		// Copier les données de l'objet PersonModel vers l'objet Etudiant (cas du
		// formulaire de l'étudiant)
		if (person.getTypePerson() == PersonModel.TYPE_STUDENT) {
			Etudiant etd = new Etudiant();
			BeanUtils.copyProperties(person, etd);

			personService.addPerson(etd);

		}
		// Copier les données de l'objet PersonModel vers l'objet Enseignant (cas du
		// formulaire de l'Enseignant)

		else if (person.getTypePerson() == PersonModel.TYPE_PROF) {
			Enseignant prof = new Enseignant();
			BeanUtils.copyProperties(person, prof);
			personService.addPerson(prof);

		}
		// Copier les données de l'objet PersonModel vers l'objet CadreAdministrateur
		// (cas du
		// formulaire de CadreAdministrateur)
		else if (person.getTypePerson() == PersonModel.TYPE_CADRE_ADMIN) {
			CadreAdministrateur ca = new CadreAdministrateur();
			BeanUtils.copyProperties(person, ca);
			personService.addPerson(ca);

		}

		// rediriger vers l'action shwoForm avec le meme type de personne
		return "redirect:/admin/showForm?typePerson=" + person.getTypePerson();
	}

	@RequestMapping(value = "updatePersonForm/{idPerson}", method = RequestMethod.GET)
	public String updatePersonForm(@PathVariable int idPerson, Model model) {

		// On reoit comme paramètre l'id de la personne à mettre à jour
		Utilisateur utl = personService.getPersonById(Long.valueOf(idPerson));

		// On construit le modèle
		PersonModel pm = new PersonModel();

		// En fonction due type de l'utilisateur à modifier
		// Ceci va nous pemettre d'afficher un formulaire adapté
		// slon le type de la personne
		if (utl instanceof Etudiant) {
			BeanUtils.copyProperties((Etudiant) utl, pm);
			pm.setTypePerson(PersonModel.TYPE_STUDENT);
		} else if (utl instanceof Enseignant) {
			BeanUtils.copyProperties((Enseignant) utl, pm);
			pm.setTypePerson(PersonModel.TYPE_PROF);
		} else if (utl instanceof CadreAdministrateur) {
			BeanUtils.copyProperties((CadreAdministrateur) utl, pm);
			pm.setTypePerson(PersonModel.TYPE_CADRE_ADMIN);
		}

		// Initialiser le modele avec la personne
		model.addAttribute("personModel", pm);

		return "admin/updateForm";
	}

	@RequestMapping(value = "serachPerson", method = RequestMethod.GET)
	public String serachPerson(@RequestParam String cin, Model model) {

		// On reoit comme paramètre l'id de la personne à mettre à jour
		Utilisateur utl = personService.getPersonByCin(cin);

		if (utl == null) {

			// Initialiser le modele avec la personne
			model.addAttribute("personModel", new ArrayList<PersonModel>());
		} else {

			// On construit le modèle
			PersonModel pm = new PersonModel();

			// En fonction due type de l'utilisateur à modifier
			// Ceci va nous pemettre d'afficher un formulaire adapté
			// slon le type de la personne
			if (utl instanceof Etudiant) {
				BeanUtils.copyProperties((Etudiant) utl, pm);
				pm.setTypePerson(PersonModel.TYPE_STUDENT);
			} else if (utl instanceof Enseignant) {
				BeanUtils.copyProperties((Enseignant) utl, pm);
				pm.setTypePerson(PersonModel.TYPE_PROF);
			} else if (utl instanceof CadreAdministrateur) {
				BeanUtils.copyProperties((CadreAdministrateur) utl, pm);
				pm.setTypePerson(PersonModel.TYPE_CADRE_ADMIN);

			}
			List<PersonModel> modelPersons = new ArrayList<PersonModel>();
			modelPersons.add(pm);
			// Initialiser le modele avec la personne
			model.addAttribute("personList", modelPersons);
		}
		return "admin/listPersons";
	}

	@RequestMapping("updatePerson")
	public String updatePerson(@Valid @ModelAttribute("personModel") PersonModel person, BindingResult bindingResult,
			Model model) {

		// En cas d'erreur
		if (bindingResult.hasErrors()) {

			return "admin/updateForm";
		}

		// On copie les données du modèle vers l'objet métier puis on appel le service
		// pour faire la mise à jour
		if (person.getTypePerson() == PersonModel.TYPE_STUDENT) {
			Etudiant etd = new Etudiant();
			BeanUtils.copyProperties(person, etd);

			personService.updatePerson(etd);

		} else if (person.getTypePerson() == PersonModel.TYPE_PROF) {
			Enseignant prof = new Enseignant();
			BeanUtils.copyProperties(person, prof);
			personService.updatePerson(prof);

		} else if (person.getTypePerson() == PersonModel.TYPE_CADRE_ADMIN) {
			CadreAdministrateur ca = new CadreAdministrateur();
			BeanUtils.copyProperties(person, ca);
			personService.updatePerson(ca);

		}

		// Mettre le message de succès dans le modèle
		model.addAttribute("msg", "Opération effectuée avec succès");

		return "admin/updateForm";
	}

	@RequestMapping("managePersons")
	public String managePersons(Model model) {

		List<Utilisateur> persons = personService.getAllPersons();
		List<PersonModel> modelPersons = new ArrayList<PersonModel>();

		// Copier les objets metier vers PersonModel plus flexible
		for (int i = 0; i < persons.size(); i++) {
			PersonModel pm = new PersonModel();
			if (persons.get(i) instanceof Etudiant) {
				BeanUtils.copyProperties((Etudiant) persons.get(i), pm);
				pm.setTypePerson(PersonModel.TYPE_STUDENT);
				modelPersons.add(pm);
			} else if (persons.get(i) instanceof Enseignant) {
				BeanUtils.copyProperties((Enseignant) persons.get(i), pm);
				pm.setTypePerson(PersonModel.TYPE_PROF);
				modelPersons.add(pm);
			} else if (persons.get(i) instanceof CadreAdministrateur) {
				BeanUtils.copyProperties((CadreAdministrateur) persons.get(i), pm);
				pm.setTypePerson(PersonModel.TYPE_CADRE_ADMIN);
				modelPersons.add(pm);
			}
		}

		model.addAttribute("personList", modelPersons);

		return "admin/listPersons";
	}

	@RequestMapping(value = "deletePerson/{idPerson}", method = RequestMethod.GET)
	public String delete(@PathVariable int idPerson) {

		personService.deletePerson(Long.valueOf(idPerson));

		return "redirect:/admin/managePersons";
	}

	@GetMapping("exportPersons")
	public void exportToExcel(HttpServletResponse response) throws IOException {
		response.setContentType("application/octet-stream");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=users_" + currentDateTime + ".xlsx";
		response.setHeader(headerKey, headerValue);

		List<Utilisateur> persons = personService.getAllPersons();

		ExcelExporter excelExporter = personService.preparePersonExport(persons);

		excelExporter.export(response);
	}
}
