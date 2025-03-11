package com.torneo.projectegestoresportsfinal.libs;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.torneo.projectegestoresportsfinal.classes.Tournament;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.Map;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.ParseException;
import java.util.*;



public class GSON {
    private final Gson gson;

    public GSON() {
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .setPrettyPrinting()
                .create();
    }




    /**
     * Comprova si la ruta del fitxer o directori passada per paràmetre existeix
     *
     * @param ruta String amb la ruta del fitxer
     * @return true -- Si existeix. False -- Si no existeix
     */
    public boolean existeix(String ruta) {
        // Comprova la existència d'un directori o fitxer
        Path path = Paths.get(ruta);        // Path --> import java.nio.file.Path;
        return Files.exists(path);          // Files --> import java.nio.file.Files

    }

    /**
     * Crea un directori a la ruta indicada per paràmetre
     *
     * @param ruta ruta del directori a crear
     * @throws IOException Excepció per d'Entrada Sortida
     */
    public void creaDirectori(String ruta) throws IOException {
        Path path = Paths.get(ruta);         // Path --> import java.nio.file.Path;
        // Paths --> import java.nio.file.Paths
        Files.createDirectory(path);    // Files --> import java.nio.file.Files
    }

    /**
     * Escriu el text "text" al fitxer "fitx"
     * !!EN AQUEST CAS S'ESCRIU EN UN JOC DE CARÀCTERS: UTF-8 !!
     *
     * @param fitx   ruta del fitxer de text
     * @param text   cadena String a escriure al fitxer
     * @param afegir Si és tru afegirem. Si és fals crearem un fitxer nou
     * @throws IOException excepció si hi ha algun error
     */
    public void escriuTextFitxer(String fitx, String text, boolean afegir) throws IOException {
        Path path = Paths.get(fitx);
        String linia = null;
        if (!existeix(fitx)) {
            Files.createFile(path);
        }
        List<StringBuffer> dades = new ArrayList<StringBuffer>();
        dades.add(new StringBuffer(text));

        // Escrivim dintre del fitxer finalment
        if (afegir)
            Files.write(path, dades, Charset.forName("UTF-8"), Files.exists(path) ? StandardOpenOption.APPEND : StandardOpenOption.CREATE);
        else
            Files.write(path, dades, Charset.forName("UTF-8"));

    }

    /**
     * Retorna un objecte JsonArray amb tot el contingut del fitxer Json
     *
     * @param fitxer nom del fitxer
     * @return JsonArray amb el contingut de tot el fiter Json
     * @throws IOException    Excepció d'I/O
     * @throws ParseException Excepció de Parser
     */
    public JsonArray retornaFitxerJson(String fitxer) throws IOException, ParseException {

        JsonParser parser = new JsonParser();            // creem el parser

        Object obj = parser.parse(new FileReader(fitxer));    // llegim el fitxer

        // convert object to JsonArray
        JsonArray JsonArray = (JsonArray) obj;            // escrivim el contingut
        // del fitxer en un JsonArray
        return JsonArray;                        // retornem el JsonArray
    }

    /**
     * Retorna un objecte JsonArray amb tot el contingut del fitxer Json
     *
     * @param fitxer nom del fitxer
     * @return JsonArray amb el contingut de tol el fiter Json
     * @throws IOException    Excepció d'I/O
     * @throws ParseException Excepció de Parser
     */
    public String retornaFitxerJsonAString(String fitxer) throws IOException, ParseException {
        JsonArray ja = retornaFitxerJson(fitxer);
        return ja.toString();                       // retornem l'String
    }

    /**
     * Escriu un String ja amb format Json a un fitxer Json
     *
     * @param rutaDesti Ruta del fitxer destí
     * @param contingut contingut de l'String amb format Json
     * @throws IOException excepció d'E/S
     */
    public void escriuStringJsonAFitxerJson(String rutaDesti, String contingut) throws IOException {
        Files.write(Paths.get(rutaDesti), contingut.getBytes());
        // false pq hem d'escriure l'arxiu sencer cada cop
    }

    /**
     * Escriu un arxiu Json beautificat apartir d'un contingut en format Json
     *
     * @param rutaDesti     ruta de l'arxiu
     * @param contingutJson contingut de l'arxiu en format Json
     * @throws IOException    excepció d'Entrada/Sortida
     * @throws ParseException excepció de Parse
     */
    public void sobreEscriuStringJsonAmbPrettyFormat(String rutaDesti,
                                                     String contingutJson) throws IOException, ParseException {


        JsonArray ja = JsonParser.parseString(contingutJson).getAsJsonArray();


        String arxiuJson = JsonAPrettyFormat(ja.toString());

        escriuTextFitxer(rutaDesti, arxiuJson, false); // false pq hem d'escriure l'arxiu sencer cada cop
    }

    /**
     * Escriu un arxiu Json beautificat apartir d'un contingut en format Json
     *
     * @param fitxerOriginal ruta de l'arxiu a convertir
     * @param fitxerDesti    ruta de l'arxiu final
     * @throws IOException    excepció d'Entrada/Sortida
     * @throws ParseException excepció de Parse
     */
    public void sobreEscriuArxiuJsonAmbPrettyFormat(String fitxerOriginal,
                                                    String fitxerDesti) throws IOException, ParseException {


        JsonArray ja = retornaFitxerJson(fitxerOriginal);

        String arxiuJson = JsonAPrettyFormat(ja.toString());

        escriuTextFitxer(fitxerDesti, arxiuJson, false); // false pq hem d'escriure l'arxiu sencer cada cop
    }

    /**
     * Escriu i acumula un objecte Json a un fitxer Json amb possibilitat de pretty.
     *
     * @param objJson    Objecte a escriure
     * @param rutaFitxer ruta del fitxer .Json
     * @throws IOException Execpció d'E/S
     */
    public void escriuObjecteJAVAAFitxerJson(String rutaFitxer,
                                             Object objJson,
                                             boolean pretty) throws IOException, ParseException {
        // per beautificar un Json:  https://codebeautify.org/Jsonviewer

        // l'objecte que volem convertir a Json entra per paràmetre del mètode
        Gson gson = new GsonBuilder().setPrettyPrinting().create(); // Pretty
        String Json = gson.toJson(objJson);                         // Passem l'objecte a String amb format Json
        JsonObject JsonObject = JsonParser.parseString(Json).getAsJsonObject();  // El convertim a JsonObject
        // passant-li un parser. Important.
        JsonArray arxiuJson = new JsonArray();    // Creem la variable JsonArray on anirà el contingut del fitxer
        //  gson.toJson(objJson);

        // TASQUES:
        // Hem d'Integrar Objecte Json en Fitxer Json
        // per tant hem de carregar primer l'arxiu amb un JsonArray, i mesclar allà el nou objecte Json
        if (existeix(rutaFitxer)) {
            arxiuJson = retornaFitxerJson(rutaFitxer);      // 1) recuperem el contingut de l'arxiu Json en un JsonArray
        }
        arxiuJson.add(JsonObject);                          // 2) afegim el nou objecte al JsonArray del fitxer

        if (pretty)
            escriuTextFitxer(rutaFitxer, JsonAPrettyFormat(arxiuJson.toString()), false);  // 3) escrivim (sobreescrivint) tot el JsonArray
        else
            escriuTextFitxer(rutaFitxer, arxiuJson.toString(), false);  // 3) escrivim (sobreescrivint) tot el JsonArray

    }

    /**
     * Mostra un String Json en 'pretty format'
     *
     * @param JsonString String Json a mostrar
     * @return String Json beautificat
     * @throws ParseException Excepció de Parse
     */
    public String JsonAPrettyFormat(String JsonString) {


//        // Json-simple
//        JsonParser parser = new JsonParser();
//        JsonArray Json = (JsonArray) parser.parse(JsonString);
        //gson
        JsonElement Json = JsonParser.parseString(JsonString);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String prettyJson = gson.toJson(Json);

        return prettyJson;
    }

    /**
     * En principi hauria de mostrar un fitxer Json per consola
     *
     * @param fitxer fitxer a mostrar
     * @throws IOException    Excepció d'E/S
     * @throws ParseException Excepció de parser
     */
    public void mostraFitxerJsonConsola(String fitxer) throws IOException, ParseException {

        JsonArray ja = retornaFitxerJson(fitxer);
        String arxiuJson = ja.toString();
        System.out.println(arxiuJson);
    }

    /**
     * En principi hauria de mostrar un fitxer Json per consola
     *
     * @param fitxer fitxer a mostrar
     * @throws IOException    Excepció d'E/S
     * @throws ParseException Excepció de parser
     */
    public void mostraFitxerJsonConsolaPrettyFormat(String fitxer) throws IOException, ParseException {

        JsonArray ja = retornaFitxerJson(fitxer);
        String arxiuJson = JsonAPrettyFormat(ja.toString());
        System.out.println(arxiuJson);
    }


    /**
     * Retorna el contingut d'un fitxer Json a una llista de JsonObject. Tots els que coincideixin amb dadaACercar.
     * CREC QUE NO FUNCIONA!!
     *
     * @param fitxer      Fitxer a
     * @param dadaACercar Camp a cercar
     * @return Llista d'objectes JsonObject
     * @throws IOException    Excepció d'E/S
     * @throws ParseException Excepció de parse
     */
    public List<JsonObject> retornaJsonLlista(String fitxer,
                                              String dadaACercar) throws IOException {

        JsonParser parser = new JsonParser();
        File file = new File(fitxer);
        FileReader reader = new FileReader(file);
        JsonObject JsonObject = (JsonObject) parser.parse(reader);

        JsonArray dades = (JsonArray) JsonObject.get(dadaACercar);

//        Map<String,String> map = new HashMap<String,String>();
//        List<Map<String, String>> lstmap = new ArrayList<Map<String, String>>();
        List<JsonObject> llista = new ArrayList<>();

        Iterator<String> iter = null;

        for (int i = 0; i < dades.size(); i++) {

            JsonObject firstarr = (JsonObject) dades.get(i);
            llista.add(firstarr);

        }
        return llista;
    }

    /**
     * Cerca una dada al fitxer i ens retorna un Jsonarray
     *
     * @param fitxer      Ruta del fitxer Json
     * @param dadaACercar Dada a cercar
     * @return retorna JsonArray que correspon amb la dada a cercar
     * @throws IOException    Excepció d'E/S
     * @throws ParseException Excepció de parse
     */
    public JsonArray cercarJson(String fitxer,
                                String dadaACercar) throws IOException {

        JsonParser parser = new JsonParser();
        File file = new File(fitxer);
        FileReader reader = new FileReader(file);
        JsonObject JsonObject = (JsonObject) parser.parse(reader);

        JsonArray dades = (JsonArray) JsonObject.get(dadaACercar);

        Map<String, String> map = new HashMap<String, String>();
        List<Map<String, String>> lstmap = new ArrayList<Map<String, String>>();

        Iterator<String> iter = null;
        Iterator<String> iter2 = null;

        return dades;

    }

    /**
     * Canvia dades que li passem per paràmetre
     *
     * @param fitxer       fitxer Json a modificar
     * @param nomCamp      nom del camp a modificar
     * @param dadaACanviar valor que volem canviar
     */
    public void canviarDadesJson(String fitxer,
                                 String nomCamp,
                                 String dadaACanviar) {


    }

    /**
     * Mostra en consola les dades que coincideixen en "dada a Cercar" dintre d'un fitxer Json
     *
     * @param fitxer      fitxer a obrir
     * @param dadaACercar dada a cercar
     * @throws IOException    Excepció d'E/S
     * @throws ParseException Excepció de parser
     */
    public void cercarJsonConsola(String fitxer,
                                  String dadaACercar) throws IOException {

        JsonParser parser = new JsonParser();
        File file = new File(fitxer);
        FileReader reader = new FileReader(file);
        JsonObject JsonObject = (JsonObject) parser.parse(reader);

        JsonArray dades = (JsonArray) JsonObject.get(dadaACercar);

        Map<String, String> map = new HashMap<String, String>();
        List<Map<String, String>> lstmap = new ArrayList<Map<String, String>>();

        Iterator<String> iter = null;
        Iterator<String> iter2 = null;

        for (int i = 0; i < dades.size(); i++) {

            JsonObject firstarr = (JsonObject) dades.get(i);

            iter = firstarr.keySet().iterator();
            //iter2 = firstarr.values().iterator();

            while (iter.hasNext()) {
                String key = (String) iter.next();
                String value = firstarr.toString();
                //String value = (String)iter2.next();

                map.put(key, value);
                lstmap.add(i, map);
                // System.out.println(i + ":  " + key + "  " + map.get(key));

            }
        }

//        for(int i=0;i<lstmap.size();i++){
//
//            Map<String,String> mapget = lstmap.get(i);
//
//            for (Map.Entry<String, String> entry : mapget.entrySet()) {
//                System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
//            }
//        }

    }


    /**
     * Passa un jsonArray a una llista d'objectes Java
     * @param jsonArray jsonArray a convertir
     * @param clazz Classe a la que es convertiran els objectes de jsonArray
     * @return Llista d'objectes del fitxer ja convertits al tiups 'T' passat per paràmetre
     * @param <T> Tipus a la que es convertiran els objectes de jsonArray
     *
     *           cridar així:
    <pre>{@code
     * List<Autobus> llista = gs.retornaJsonArrayALlistaObjecte(jsonArray, Autobus.class);
     * }</pre>     */
    public <T> List<T> retornaJsonArrayALlistaObjecte(JsonArray jsonArray,
                                                      Class<T> clazz) {
        Gson gson=new Gson();
        Type type = TypeToken.getParameterized(List.class, clazz).getType();
        return gson.fromJson(jsonArray, type);
    }


    /**
     * Retorna una llista d'objecte java a partir d'una ruta de fitxer json i un tipus de dada
     *
     * @param rutaFitxer Ruta del fitxer json
     * @param clazz Classe a la que es convertiran els objectes de jsonArray
     * @return Llista d'objectes del fitxer ja convertits al tiups 'T' passat per paràmetre
     * @param <T> Tipus a la que es convertiran els objectes de jsonArray
     * @throws IOException Excepció d'Entrada/Sortida
     * @throws ParseException Excepció de Parse
     *
     *      *           cridar així:
     *     <pre>{@code
     *      * List<Autobus> llista = gs.retornaFitxerJsonALlistaObjecte("fitxer.json", Autobus.class);
     *      * }</pre>
     */
    public <T> List<T> retornaFitxerJsonALlistaObjecte(String rutaFitxer,
                                                       Class<T> clazz) throws IOException, ParseException {
        JsonArray jsonArray=retornaFitxerJson(rutaFitxer);
        return retornaJsonArrayALlistaObjecte(jsonArray,clazz);

    }


    public void escriuMapAFitxerJson(String ruta, Map<String, Tournament> tournamentsMap) throws IOException {
        try (FileWriter writer = new FileWriter(ruta)) {
            gson.toJson(tournamentsMap, writer);
        }
    }

    public Map<String, Tournament> retornaFitxerJsonAMap(String fitxerJson) throws IOException {
        try (FileReader reader = new FileReader(fitxerJson)) {
            Type mapType = new TypeToken<Map<String, Tournament>>(){}.getType();
            return gson.fromJson(reader, mapType);
        }
    }



}
