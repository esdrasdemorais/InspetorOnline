package br.com.octabus.Util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by marcatti on 25/06/17.
 */

public class DateHelper {

    public static long diferenceHour(String dateOpen, String dateClose)
    {
        try
        {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dataDe = sdf.parse(dateOpen);
            Date dataAte = sdf.parse(dateClose);

//            long diferencaSegundos = (dataAte.getTime() - dataDe.getTime()) / (1000);
//            long diferencaMinutos = (dataAte.getTime() - dataDe.getTime()) / (1000*60);
            long diferencaHoras = (dataAte.getTime() - dataDe.getTime()) / (1000*60*60);
//            long diferencaDias = (dataAte.getTime() - dataDe.getTime()) / (1000*60*60*24);
//            long diferencaMeses = (dataAte.getTime() - dataDe.getTime()) / (1000*60*60*24) / 30;
//            long diferencaAnos = ((dataAte.getTime() - dataDe.getTime()) / (1000*60*60*24) / 30) / 12;
//
//            System.out.println(String.format("Diferença em Segundos: ", diferencaSegundos));
//            System.out.println(String.format("Diferença em Minutos: ", diferencaMinutos));
//            System.out.println(String.format("Diferença em Horas: ", diferencaHoras));
//            System.out.println(String.format("Diferença em Dias: ", diferencaDias));
//            System.out.println(String.format("Diferença em Meses: ", diferencaMeses));
//            System.out.println(String.format("Diferença em Anos: ", diferencaAnos));

            return diferencaHoras;
        }
        catch (ParseException e)
        {
            e.printStackTrace();
            return 0;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return 0;
        }
    }


    public static String getCurrentDate()
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();

        return (String) dateFormat.format(date);
    }
}
