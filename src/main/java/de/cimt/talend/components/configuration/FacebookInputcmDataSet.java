package de.cimt.talend.components.configuration;

import java.io.Serializable;

import lombok.Data;
import org.talend.sdk.component.api.configuration.Option;
import org.talend.sdk.component.api.configuration.type.DataSet;
import org.talend.sdk.component.api.meta.Documentation;

@DataSet("default")
@Documentation("")
@Data
public class FacebookInputcmDataSet implements Serializable {

    //
    // fill the reusable configuration for input/output components
    // -> it must also enable to instantiate a source component without additional *required* configuration
    // -> any input/output components must have a reference to a dataset to be valid for Talend Platform (cloud)
    //

    @Option
    @Documentation("")
    private FacebookInputcmDataStore connection;


}