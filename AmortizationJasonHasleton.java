package amortization.jasonhasleton;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class AmortizationJasonHasleton
{

    public static void main(String[] args)
    {

        String name, address, city, state, zip;
        int origMonth, origDay, origYear, payCode;
        double loanAmt, aprNum, apr;

        InputFile loanData;
        loanData = new InputFile("fcrc loan data.txt");

        DecimalFormat df = new DecimalFormat("0.00");
        NumberFormat cf = NumberFormat.getCurrencyInstance();

        JFrame jf = new JFrame();
        JTextArea jta = new JTextArea();
        JScrollPane jsp = new JScrollPane(jta);

        jf.setSize(550, 600);
        jf.setLocation(400, 250);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.add(jsp);
        jf.setVisible(true);

        while (!loanData.eof())
        {
            name = loanData.readString();
            address = loanData.readString();
            city = loanData.readString();
            state = loanData.readWord();
            zip = loanData.readWord();
            origMonth = loanData.readInt();
            origDay = loanData.readInt();
            origYear = loanData.readInt();
            loanAmt = loanData.readDouble();
            aprNum = loanData.readDouble();
            payCode = loanData.readInt();
            apr = aprNum * 100;
            displayHeading(name, address, city, state, zip, loanAmt, apr, jta, df, cf);
            displayTable(origMonth, origDay, origYear, aprNum, payCode, loanAmt, jta, cf);
        }

    }

    public static void displayHeading(String name, String address, String city, String state, String zip, double loanAmt, double apr, JTextArea jta, DecimalFormat df, NumberFormat cf)
    {

        jta.append("First Community Redevelopment Corporation\n101 1st Street\n");
        jta.append("Bloomingville, TN 41663\n\n");
        jta.append("\t\t\t\t" + name + "\n\t\t\t\t" + address + "\n");
        jta.append("\t\t\t\t" + city + ", " + state + " " + zip + "\n\n");
        jta.append("LOAN AMOUNT:\t" + cf.format(loanAmt) + "\n" + "INTEREST RATE:\t" + df.format(apr) + "%\n\n");
        jta.append("Payment#\tDue Date\tPayment\tInterest\tPrinciple\tBalance\n");
        jta.append("-------------------------------------------------------------------------------------------------------------------------------\n");

    }

    public static void displayTable(int month, int day, int year, double apr, int payCode, double loanAmt, JTextArea jta, NumberFormat cf)
    {
        double pymt, pymtTotal = 0, intTotal = 0, bal = loanAmt, intPymt, princPymt;
        int pymtNum = 1;
        String dueDate;

        double[] pcVal;
        pcVal = new double[10];
        pcVal[0] = 50.00;
        pcVal[1] = 55.00;
        pcVal[2] = 75.00;
        pcVal[3] = 100.00;
        pcVal[4] = .05 * loanAmt;
        pcVal[5] = .06 * loanAmt;
        pcVal[6] = .05 * loanAmt;
        pcVal[7] = .04 * loanAmt;
        pcVal[8] = .03 * loanAmt;
        pcVal[9] = .02 * loanAmt;

        pymt = pcVal[payCode];

        String[] date;
        date = new String[13];
        date[1] = "JAN 1, ";
        date[2] = "FEB 1, ";
        date[3] = "MAR 1, ";
        date[4] = "APR 1, ";
        date[5] = "MAY 1, ";
        date[6] = "JUN 1, ";
        date[7] = "JUL 1, ";
        date[8] = "AUG 1, ";
        date[9] = "SEP 1, ";
        date[10] = "OCT 1, ";
        date[11] = "NOV 1, ";
        date[12] = "DEC 1, ";

        do
        {
            month++;
            if (month > 12)
            {
                month = 1;
                year++;
            }

            dueDate = date[month] + year;
            intPymt = apr * bal / 12;
            princPymt = pymt - intPymt;
            bal -= princPymt;

            jta.append("     " + pymtNum + "\t" + dueDate + "\t" + cf.format(pymt) + "\t" + cf.format(intPymt) + "\t" + cf.format(princPymt) + "\t" + cf.format(bal) + "\n");

            pymtNum++;
            pymtTotal += pymt;
            intTotal += intPymt;

        } while (bal > pymt);

        month++;
        if (month > 12)
        {
            month = 1;
            year++;
        }

        dueDate = date[month] + year;
        pymt = bal;
        intPymt = apr * bal / 12;
        princPymt = pymt - intPymt;
        bal = 0;
        pymtTotal += pymt;
        intTotal += intPymt;

        jta.append("     " + pymtNum + "\t" + dueDate + "\t" + cf.format(pymt) + "\t" + cf.format(intPymt) + "\t" + cf.format(princPymt) + "\t" + cf.format(bal) + "\n\n");
        jta.append("\tTOTALS:\t" + cf.format(pymtTotal) + "\t" + cf.format(intTotal) + "\n");
        jta.append("_______________________________________________________________________________\n\n\n\n\n");

    }

}
