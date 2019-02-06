package de.cimt.talend.components.configuration;

import java.io.Serializable;

import lombok.Data;
import org.talend.sdk.component.api.configuration.Option;
import org.talend.sdk.component.api.configuration.type.DataStore;
import org.talend.sdk.component.api.configuration.ui.layout.GridLayout;
import org.talend.sdk.component.api.meta.Documentation;

@GridLayout({
        @GridLayout.Row({"ACCESS_TOKEN"})

})
@DataStore("default")
@Documentation("")
@Data
public class FacebookInputcmDataStore implements Serializable {
    // fill the datastore/connection configuration

    @Option
    @Documentation("Access token")
    private String ACCESS_TOKEN;

}