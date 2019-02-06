package de.cimt.talend.components.service;

import de.cimt.talend.components.source.CUSTOM_VALUESConfiguration;
import de.cimt.talend.components.source.InputCmMapperConfiguration;
import de.cimt.talend.components.util.FacebookConstants;
import org.talend.sdk.component.api.service.Service;
import org.talend.sdk.component.api.service.completion.DynamicValues;
import org.talend.sdk.component.api.service.completion.Values;

import java.util.Arrays;

@Service
public class FacebookInputcmService {

    // you can put logic here you can reuse in components
    // drop down list values

    @DynamicValues(FacebookConstants.PAGE_CHOSED)
    public Values getPAGE_CHOSED() {
        return new Values(Arrays.asList(new Values.Item("ME", "Me"),
                new Values.Item("ID", "Others")));
    }

    @DynamicValues(FacebookConstants.VERSION_CHOSED)
    public Values getVERSION_CHOSED() {
        return new Values(Arrays.asList(new Values.Item("v3.2", "v3.2"), new Values.Item("v3.0", "v3.0"), new Values.Item("v2.10", "v2.10"),
                new Values.Item("v2.09", "v2.09"), new Values.Item("v2.08", "v2.08")));
    }

    @DynamicValues(FacebookConstants.METHOD_CHOSED)
    public Values getMETHOD_CHOSED() {
        return new Values(Arrays.asList(new Values.Item("GET", "GET"),
                new Values.Item("POST", "POST")));
    }


    public String configureURL(InputCmMapperConfiguration configuration) {
        String fields = "?fields=" + configuration.getFIELDS();

        String access_token; //  //="&access_token=";
        String page = null;
        //the string starts with the host url +version ....

        String connString = FacebookConstants.HOST + configuration.getVERSION() + "/";

        if (configuration.getPAGE_CHOSED().toUpperCase().equals("ME")) {
            page = "me";
        } else {
            if (configuration.getPAGE() == null) {
                if (!configuration.isDIE_ON_ERROR()) {
                    throw new RuntimeException("Page cannot be empty please enter page id");
                } else {
                    System.err.println("Invalid page/user id");
                }
            } else {
                page = configuration.getPAGE();
            }
        }
        //if there are no fields access token is
        //constructed as such.

        if (configuration.getFIELDS().isEmpty() && configuration.getEDGE().isEmpty()) {
            access_token = "?access_token=" + configuration.getDataset().getConnection().getACCESS_TOKEN();
            connString += page + access_token;
        } else if (!configuration.getFIELDS().isEmpty()) {
            access_token = "&access_token=" + configuration.getDataset().getConnection().getACCESS_TOKEN();
            connString += page + "/" + fields + access_token;
        } else if (configuration.getFIELDS().isEmpty() && !configuration.getEDGE().isEmpty()) {
            access_token = "?access_token=" + configuration.getDataset().getConnection().getACCESS_TOKEN();
            connString += page + "/" + configuration.getEDGE() + access_token;
        } else if (!configuration.getFIELDS().isEmpty() && !configuration.getEDGE().isEmpty()) {
            access_token = "&access_token=" + configuration.getDataset().getConnection().getACCESS_TOKEN();
            connString += page + "/" + configuration.getEDGE() + fields + access_token;

        } else if (!configuration.getFIELDS().isEmpty() && configuration.getEDGE().isEmpty()) {
            access_token = "&access_token=" + configuration.getDataset().getConnection().getACCESS_TOKEN();
            connString += page + "/" + fields + access_token;

        }


        if (configuration.getCUSTOM_VALUES() != null) {
            String props = "";
            for (CUSTOM_VALUESConfiguration conf : configuration.getCUSTOM_VALUES()) {
                if (!conf.getPROPERTY().isEmpty() && !conf.getVALUE().isEmpty()) {
                    props += "&" + conf.getPROPERTY() + "=" + conf.getVALUE();
                } else {
                    if (!configuration.isDIE_ON_ERROR()) {
                        System.err.println("Invalid key/value pairs in Properties");
                    } else {
                        throw new RuntimeException("Invalid Value in Additional Properties, Key/Value Cannot be empty");
                    }
                }

            }
            //assign additional properties only if any are set
            connString += props;
        }

        System.out.println(connString);

        return connString;


    }


}