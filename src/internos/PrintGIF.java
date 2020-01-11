/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package internos;

    import java.io.*;
    import javax.print.*;
    import javax.print.attribute.*;
    import javax.print.attribute.standard.*;

    /*
    *  Use the Java(TM) Print Service API to locate a print service which
    *  can print a GIF-encoded image. A GIF image is printed according to
    *  a job template specified as a set of printing attributes.
    */
    public class PrintGIF {

            public static void main(String args[]) {

                    /* Use the pre-defined flavor for a GIF from an InputStream */
                    DocFlavor flavor = DocFlavor.INPUT_STREAM.GIF;

                    /* Create a set which specifies how the job is to be printed */
                    PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
                    aset.add(MediaSizeName.NA_LETTER);
                    aset.add(new Copies(1));

                    /* Locate print services which can print a GIF in the manner specified */
                    PrintService[] pservices =
                            PrintServiceLookup.lookupPrintServices(flavor, aset);

                    if (pservices.length > 0) {
                            /* Create a Print Job */
                            DocPrintJob printJob = pservices[0].createPrintJob();
                            /* Create a Doc implementation to pass the print data */
                            Doc doc = new InputStreamDoc("uno.gif", flavor);

                            /* Print the doc as specified */
                            try {
                                    printJob.print(doc, aset);
                            } catch (PrintException e) { 
                                    System.err.println(e);
                            }
                    } else {
                            System.err.println("No suitable printers");
                    }
            }
    }

    class InputStreamDoc implements Doc {
            private String filename;
            private DocFlavor docFlavor;
            private InputStream stream;

            public InputStreamDoc(String name, DocFlavor flavor) {
                    filename = name;
                    docFlavor = flavor;
            }
       
            @Override
            public DocFlavor getDocFlavor() { 
                    return docFlavor;
            }

            /* No attributes attached to this Doc - mainly useful for MultiDoc */
            @Override
            public DocAttributeSet getAttributes() {
                    return null;
            }

            /* Since the data is to be supplied as an InputStream delegate to
            * getStreamForBytes().
            */
            @Override
            public Object getPrintData() throws IOException {
                    return getStreamForBytes();
            }

            /* Not possible to return a GIF as text */
            @Override
            public Reader getReaderForText()
                    throws UnsupportedEncodingException, IOException {
            return null;
            }

            /* Return the print data as an InputStream.
            * Always return the same instance.
            */
            @Override
            public InputStream getStreamForBytes() throws IOException {
                    synchronized(this) {
                            if (stream == null) {
                                    stream = new FileInputStream(filename);
                            }
                            return stream;
                    } 
            }
    }
