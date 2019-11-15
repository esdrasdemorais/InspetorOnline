package com.esdrasmorais.inspetoronline.data;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

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
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import bolts.Continuation;
import bolts.Task;

public class CsvUtil {

    private static final String TAG = CsvUtil.class.getSimpleName();

    private static List<String[]> companiesStringArray;

    private static List<Line> lines = new ArrayList<Line>();

    private static List<Vehicle> vehicles = new ArrayList<Vehicle>();

    private static List<Company> companies = new ArrayList<>();

    public CsvUtil() {
        companiesStringArray = new ArrayList<String[]>();
        setCompanies();
    }

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
                //Which column you want to export
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

    private static Reader getCompaniesCsvFile(String tableName) {
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

    private static class GetCompaniesCsvFile
        extends AsyncTask<String, Integer, Reader>
    {
        @Override
        protected Reader doInBackground(String... tableName) {
            return getCompaniesCsvFile(tableName[0]);
        }

        @Override
        protected void onPostExecute(Reader reader) {
            importCompanyFromCsv(reader);
        }
    }

    private static void importCompanyFromCsv(Reader reader) {
        //String[] nextLine;
        try {
            CsvReader csvReader = new CsvReader(reader);
            // nextLine[] is an array of values from the line
            while (csvReader.getHasNext()) {
                csvReader.readNext(companiesStringArray);
            }
            /*ExecutorService service =  Executors.newSingleThreadExecutor();
            service.submit(new Runnable() {
                @Override
                public void run() {
                    //Task.callInBackground((Callable<Void>) () -> {
                        csvReader.getReadNext().observeForever(new Observer<String[]>() {
                            @Override
                            public void onChanged(String[] readNext) {
                                if (readNext != null)
                                    setCompany(readNext);
                            }
                        });*/
                        /*return null;
                    }).continueWith((Continuation<Void, Void>) task -> {
                        if (task.isFaulted()) {
                            Log.e(TAG, "find failed", task.getError());
                        }
                        return null;
                    });*/
              /*  }
            });*/
        } catch (IOException ex) {
            try {
                throw new IOException(ex.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void setCompany(String[] nextLine) {
        Company company = new Company();
        company.setId(UUID.randomUUID().toString().replace("-", ""));
        try {
            company.setOperationAreaCode(Integer.parseInt(nextLine[0]));
            company.setCompanyReferenceCode(Integer.parseInt(nextLine[1]));
        } catch (NumberFormatException ex) {
            Log.e(TAG, "setCompany(): " + ex.getMessage());
        }
        company.setCompanyName(nextLine[2]);
        companies.add(company);
    }

    private void setCompanies() {
        new GetCompaniesCsvFile().execute("companies.csv");
    }

    public static List<Company> getCompanies() {
        for (String[] company : companiesStringArray) {
            setCompany(company);
        }
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
            //csvReader.readNext();
            // nextLine[] is an array of values from the line
            while (csvReader.getHasNext()) {
                //csvReader.readNext();
                if (csvReader.getReadNext().getValue() != null)
                    setLine(csvReader.getReadNext().getValue());
            }
        } catch (Exception ex) {
            Log.d("CsvUtil", ex.getMessage());
        }
    }

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
            //csvReader.readNext();
            // nextLine[] is an array of values from the line
            while (csvReader.getHasNext()) {
                //csvReader.readNext();
                if (csvReader.getReadNext().getValue() != null)
                    setVehicle(csvReader.getReadNext().getValue(), 6);
            }
        } catch (Exception ex) {
            Log.d("CsvUtil", ex.getMessage());
        }
    }

    public static List<Vehicle> getVehicles() {
        importVehicleFromCsv("lines.csv");
        return vehicles;
    }
}