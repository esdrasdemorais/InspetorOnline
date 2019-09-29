package com.esdrasmorais.inspetoronline.data;

import android.database.Cursor;
import android.os.Environment;
import android.util.Log;

import com.esdrasmorais.inspetoronline.data.model.Company;
import com.esdrasmorais.inspetoronline.data.model.Direction;
import com.esdrasmorais.inspetoronline.data.model.Line;
import com.esdrasmorais.inspetoronline.data.model.Vehicle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class CsvUtil {
    private static void exportTableToCsv(
        AppDatabase db, String fileName, String tableName
    ) {
        File exportDir = new File(Environment.getExternalStorageDirectory(), "");
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }
        File file = new File(exportDir, fileName + ".csv");
        try {
            file.createNewFile();
            CsvWriter csvWrite = new CsvWriter(new FileWriter(file));
            Cursor curCSV = db.query("SELECT * FROM " + tableName, null);
            csvWrite.writeNext(curCSV.getColumnNames());
            while (curCSV.moveToNext()) {
                //Which column you want to exprort
                String arrStr[] = new String[curCSV.getColumnCount()];
                for (int i = 0; i < curCSV.getColumnCount() - 1; i++)
                    arrStr[i] = curCSV.getString(i);
                csvWrite.writeNext(arrStr);
            }
            csvWrite.close();
            curCSV.close();
        } catch (Exception sqlEx) {
            Log.e("CsvUtil", sqlEx.getMessage(), sqlEx);
        }
    }

    static List<String> values = null;

    private static Reader getCompaniesCsvFile(String tableName) {
        //Environment.getExternalStorageDirectory()
        BufferedReader r = null;
        try {
            URL url = new URL(
            "https://firebasestorage.googleapis.com/v0/b/" +
                "inspetoronline.appspot.com/o/" + tableName + "?alt=media&" +
                "token=e1349741-1d95-40d5-a382-74b1191f7993"
            );
            r = new BufferedReader(
                new InputStreamReader(url.openStream())
            );
        } catch (MalformedURLException mex) {
            Log.e("CsvUtil", mex.getMessage());
        } catch (IOException ex) {
            Log.e("CsvUtil", ex.getMessage());
        }
        return r;
    }

    private static void importCompanyFromCsv(String tableName) {
        String[] nextLine;
        try {
            CsvReader csvReader = new CsvReader(
                getCompaniesCsvFile(tableName)
            );
            csvReader.readNext();
            // nextLine[] is an array of values from the line
            while ((nextLine = csvReader.readNext()) != null) {
                setCompany(nextLine);
            }
        } catch (Exception ex) {
            Log.d("CsvUtil", "Error: " +
                ex.getMessage() + ex.getStackTrace());
        }
    }

    private static void setCompany(String[] nextLine) {
        Company company = new Company();
        company.setOperationAreaCode(Integer.parseInt(nextLine[0]));
        company.setCompanyReferenceCode(Integer.parseInt(nextLine[1]));
        company.setCompanyName(nextLine[2]);
        companies.add(company);
    }

    static List<Company> companies = new ArrayList<Company>();

    public static List<Company> getCompanies() {
        importCompanyFromCsv("companies.csv");
        return companies;
    }

    private static void setLine(String[] nextLine) {
        Line line = new Line();
        line.setShortName(nextLine[0]);
        line.setLineCode(Integer.parseInt(nextLine[1]));
        line.setDirection(Direction.of(nextLine[2]));
        line.setLineDestinationMarker(nextLine[3]);
        line.setLineOriginMarker(nextLine[4]);
        line.setVehiclesQuantityLocalized(Integer.parseInt(nextLine[5]));
        lines.add(line);
    }

    private static String getLinesCsvPath() {
        return "https:////firebasestorage.googleapis.com//v0//b//" +
            "inspetoronline.appspot.com//o//lines.csv?alt=media&" +
            "token=d840e855-005b-4f98-94ca-d6d0f007cfbd";
    }

    private static void importLineFromCsv(String tableName) {
        String[] nextLine;
        try {
            CsvReader csvReader = new CsvReader(
                new FileReader(getLinesCsvPath() + "/" + tableName)
            );
            csvReader.readNext();
            // nextLine[] is an array of values from the line
            while ((nextLine = csvReader.readNext()) != null) {
                setLine(nextLine);
            }
        } catch (Exception ex) {
            Log.d("CsvUtil", ex.getMessage());
        }
    }

    static List<Line> lines = new ArrayList<Line>();

    public static List<Line> getLines() {
        importLineFromCsv("lines.csv");
        return lines;
    }

    private static void setVehicle(String[] nextLine, Integer i) {
        if (nextLine[i].trim().length() > 0) {
            setVehicle(nextLine, i += 5);
        } else {
            Vehicle vehicle = new Vehicle();
            vehicle.setPrefix(Integer.parseInt(nextLine[i]));
            vehicle.setHandicappedAccessible(Boolean.parseBoolean(nextLine[i + 1]));
            vehicle.setLocalizatedAt(Date.valueOf(nextLine[i + 2]));
            vehicle.setLatitude(Double.parseDouble(nextLine[i + 3]));
            vehicle.setLongitude(Double.parseDouble(nextLine[i + 4]));
            vehicles.add(vehicle);
        }
    }

    private static void importVehicleFromCsv(String tableName) {
        String[] nextLine;
        try {
            CsvReader csvReader = new CsvReader(
                new FileReader(
                        Environment.getExternalStorageDirectory() + "/" + tableName
                )
            );
            csvReader.readNext();
            // nextLine[] is an array of values from the line
            while ((nextLine = csvReader.readNext()) != null) {
                setVehicle(nextLine, 6);
            }
        } catch (Exception ex) {
            Log.d("CsvUtil", ex.getMessage());
        }
    }

    static List<Vehicle> vehicles = new ArrayList<Vehicle>();

    public static List<Vehicle> getVehicles() {
        importVehicleFromCsv("lines.csv");
        return vehicles;
    }
}