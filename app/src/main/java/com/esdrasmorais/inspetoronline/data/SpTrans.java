package com.esdrasmorais.inspetoronline.data;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.esdrasmorais.inspetoronline.data.model.Company;
import com.esdrasmorais.inspetoronline.data.model.Line;
import com.esdrasmorais.inspetoronline.data.model.Vehicle;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class SpTrans {
    private JsonObject json;
    private RequestQueue requestQueue;
    private String url;
    private Context context;
    private GetVolleyResponse getVolleyResponse;
    private String cookie;
    private List<Company> companies = new ArrayList<Company>();
    private Integer countCompanies = 1;
    private List<Line> lines = new ArrayList<Line>();
    private List<Vehicle> prefixes = new ArrayList<Vehicle>();

    public SpTrans(Context context, Location location) {
        this.context = context;
        this.url = "http://api.olhovivo.sptrans.com.br/v2.1";
        this.getVolleyResponse = new GetVolleyResponse(context);
        this.authenticate();
    }

    public String getApiToken() {
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(
                context.getPackageName(), PackageManager.GET_META_DATA
            );
            if (appInfo.metaData != null) {
                return appInfo.metaData.getString("br.com.sptrans.api.TOKEN");
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("SpTrans", e.getMessage());
        }
        return "";
    }

    public String getUrl() {
        return this.url;
    }

    public JsonObject getJson() {
        return this.json;
    }

    private void setJson(JsonObject json) {
        this.json = json;
    }

    public List<Company> getCompanyList() {
        return this.companies;
    }

    public List<Line> getLineList() {
        return this.lines;
    }

    public List<Vehicle> getPrefixList() {
        return this.prefixes;
    }

    private void setCompanies() {
        Company company = new Company();
        company.setOperationAreaCode(1);
        company.setCompanyReferenceCode(38);
        company.setCompanyName("SANTA BRIGIDA");
        companies.add(company);
    }

    private void setPrefixes(JsonArray prefixes) {
        try {
            for (JsonElement prefix : prefixes) {
                JsonObject prefixObject = prefix.getAsJsonObject();

                Vehicle vehicle = new Vehicle();
                vehicle.setPrefix(Integer.parseInt(
                        prefixObject.get("p").toString())
                );
                vehicle.setHandicappedAccessible(Boolean.parseBoolean(
                        prefixObject.get("a").toString()
                ));

                SimpleDateFormat sdf = new SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss'Z'"
                );
                sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                Date localizedAt = sdf.parse(
                        prefixObject.get("ta").toString().replace("\"", "")
                );
                vehicle.setLocalizatedAt(localizedAt);

                vehicle.setLatitude(Double.parseDouble(
                        prefixObject.get("py").toString())
                );
                vehicle.setLongitude(Double.parseDouble(
                        prefixObject.get("px").toString()
                ));
                this.prefixes.add(vehicle);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void setLines() {
        final JsonArray lines = this.getJson().get("l").getAsJsonArray();

        if (!lines.isJsonArray())
            throw new IllegalArgumentException("json is not an array");

        for (JsonElement line : lines) {
            JsonObject lineObject = line.getAsJsonObject();

            Line lineModel = new Line();
            String shortName = lineObject.get("c").toString();
            lineModel.setShortName(shortName.replace("\"", ""));
            lineModel.setLineCode(Integer.parseInt(lineObject.get("cl").toString()));
            lineModel.setName(
                    lineObject.get("lt0").toString().replace("\"", "")
            );
            lineModel.setLineDestinationMarker(
                    lineObject.get("lt0").toString().replace("\"", "")
            );
            lineModel.setLineOriginMarker(
                    lineObject.get("lt1").toString().replace("\"", "")
            );
            lineModel.setVehiclesQuantityLocalized(
                    Integer.parseInt(lineObject.get("qv").toString())
            );
            this.lines.add(lineModel);

            final JsonArray prefixes = lineObject.getAsJsonArray("vs");
            this.setPrefixes(prefixes);
        }
    }

    private void setLinesFromCompanies() {
        for (Company company : companies) {
            getVolleyResponse.getResponse(
                Request.Method.GET, this.getUrl() +
                    "/Posicao/Garagem?codigoEmpresa=" +
                    company.getCompanyReferenceCode(), null,
                new GetVolleyResponse(this.context) {
                    @Override
                    public void onSuccessResponse(String result) {
                        try {
                            JSONObject response = new JSONObject(result);
                            setJson(
                                new Gson().fromJson(result, JsonObject.class)
                            );
                            setLines();
                            countCompanies++;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            );
        }
    }

    private void getCompanies() {
        getVolleyResponse.getResponse(
            Request.Method.GET, this.getUrl() + "/Empresa", null,
            new GetVolleyResponse(this.context) {
                @Override
                public void onSuccessResponse(String result) {
                    try {
                        JSONObject response = new JSONObject(result);
                        setJson(
                            new Gson().fromJson(result, JsonObject.class)
                        );
                        setCompanies();
                        setLinesFromCompanies();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        );
    }

    private void authenticate() {
        String url = this.getUrl() + "/Login/Autenticar?token=" +
           this.getApiToken();
        getVolleyResponse.getResponse(Request.Method.POST, url, null,
            new GetVolleyResponse(this.context) {
                @Override
                public void onSuccessResponse(String result) {
                    try {
                        //JSONObject response = new JSONObject(result);
                        //spTrans.setJson(new Gson().fromJson(result, JsonObject.class));
                        if (Boolean.parseBoolean(result) == true) {
                            cookie = getVolleyResponse.getCookie();
                            getCompanies();
                        }
                        //notifyPropertyChanged(BR.json);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        );
    }
}