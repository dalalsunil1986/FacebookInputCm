package de.cimt.talend.components.source;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.talend.sdk.component.api.configuration.Option;
import org.talend.sdk.component.api.input.Producer;
import org.talend.sdk.component.api.meta.Documentation;
import org.talend.sdk.component.api.record.Record;
import org.talend.sdk.component.api.service.record.RecordBuilderFactory;


import de.cimt.talend.components.service.FacebookInputcmService;

@Documentation("")
public class InputCmSource implements Serializable {
    private final InputCmMapperConfiguration configuration;
    private final FacebookInputcmService service;
    private final RecordBuilderFactory builderFactory;
    private URL url;
    private HttpURLConnection httpURLConnection;
    private ListIterator<Record> records;


    public InputCmSource(@Option("configuration") final InputCmMapperConfiguration configuration,
                         final FacebookInputcmService service,
                         final RecordBuilderFactory builderFactory) {
        this.configuration = configuration;
        this.service = service;
        this.builderFactory = builderFactory;
    }

    @PostConstruct
    public void init() {
        connect();
        // this method will be executed once for the whole component execution,
        // this is where you can establish a connection for instance
        String results = read();
        List<Record> recordList = new ArrayList<>();
        recordList.add(builderFactory.newRecordBuilder().withString("ResponseContent", results).build());
        records = recordList.listIterator();
    }

    @Producer
    public Record next() {
        // this is the method allowing you to go through the dataset associated
        // to the component configuration
        //
        // return null means the dataset has no more data to go through
        // you can use the builderFactory to create a new Record.
        return records.hasNext() ? records.next() : null;
    }

    @PreDestroy
    public void release() {
        // this is the symmetric method of the init() one,
        // release potential connections you created or data you cached
    }


    // configure the http connection object
    public void connect() {

        try {
            url = new URL(service.configureURL(this.configuration));
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod(configuration.getMETHOD());
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setUseCaches(false);

            if (configuration.isTIMEOUT_SETTING() && !configuration.getCONNECT_TIMEOUT().isEmpty()) {
                int timeout = Integer.valueOf(configuration.getCONNECT_TIMEOUT());
                httpURLConnection.setConnectTimeout(timeout * 1000);
            }
        } catch (Exception ex) {
            if (this.configuration.isDIE_ON_ERROR()) {
                throw new RuntimeException(ex.getStackTrace().toString());
            } else {
                System.err.println("Connection Failed :" + ex.getStackTrace());
            }
        }

    }

    //connect and read api values
    public String read() {
        StringBuilder outPut = null;
        int bos_buffer = 0;
        byte[] buffer = null;
        String responseMessage = null;

        try {
            httpURLConnection.connect();
            buffer = new byte[1024];
            outPut = new StringBuilder();
            responseMessage = httpURLConnection.getResponseMessage();
            if (HttpURLConnection.HTTP_OK == httpURLConnection.getResponseCode()) {
                InputStream bis = new BufferedInputStream(httpURLConnection.getInputStream());
                OutputStream bosContent = null;
                //write response to file
                if (configuration.isOUT_TO_FILE() && !configuration.getOUT_FILE_NAME().isEmpty()) {
                    File bosParent = new File(configuration.getOUT_FILE_NAME());
                    if (bosParent != null && bosParent.exists() == false) {
                        bosParent.mkdirs();
                    }

                    bosContent = new BufferedOutputStream(new FileOutputStream(configuration.getOUT_FILE_NAME()));
                }
                while ((bos_buffer = bis.read(buffer)) != -1) {
                    if (configuration.isOUT_TO_FILE() && !configuration.getOUT_FILE_NAME().isEmpty()) {
                        bosContent.write(buffer, 0, bos_buffer);
                    }
                    outPut.append(new String(buffer, 0, bos_buffer));
                }
                if (configuration.isOUT_TO_FILE() && !configuration.getOUT_FILE_NAME().isEmpty()) {
                    bosContent.flush();
                    bosContent.close();
                }
            } else {
                if (!configuration.isDIE_ON_ERROR()) {
                    System.err.println(httpURLConnection.getResponseCode() + "  " + responseMessage);

                } else {
                    throw new RuntimeException("Request Failed : " + httpURLConnection.getResponseCode() + " " + responseMessage);
                }
            }


        } catch (Exception ex) {

            if (!configuration.isDIE_ON_ERROR()) {
                System.err.println("Error :" + ex);

            } else {
                throw new RuntimeException("Error  :" + ex);
            }
        }


        return outPut.toString();

    }
}