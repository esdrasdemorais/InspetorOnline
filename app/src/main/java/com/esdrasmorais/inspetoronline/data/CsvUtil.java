package com.esdrasmorais.inspetoronline.data;

import android.database.Cursor;
import android.os.Environment;
import android.util.Log;

import com.esdrasmorais.inspetoronline.data.model.Company;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
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

    static List<String>  values = null;

    private static void importCompanyFromCsv(String tableName) {
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
                values = new ArrayList<>();
                setCompany(nextLine);
            }
        } catch (Exception ex) {
            Log.d("CsvUtil", ex.getMessage());
        }
    }

    private static void setCompany(String[] nextLine) {
        Company company = new Company();
        company.setOperationAreaCode(Integer.parseInt(values.get(0)));
        company.setCompanyReferenceCode(Integer.parseInt(values.get(1)));
        company.setCompanyName(values.get(2));
        companies.add(company);
    }

    static List<Company> companies = new ArrayList<Company>();

    public static List<Company> getCompanies() {
        importCompanyFromCsv("companies.csv");
        return companies;
    }
}