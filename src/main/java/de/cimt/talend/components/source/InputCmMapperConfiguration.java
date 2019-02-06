package de.cimt.talend.components.source;

import java.io.Serializable;
import java.util.List;

import de.cimt.talend.components.configuration.FacebookInputcmDataSet;

import de.cimt.talend.components.util.FacebookConstants;
import lombok.Data;
import org.talend.sdk.component.api.component.Version;
import org.talend.sdk.component.api.configuration.Option;
import org.talend.sdk.component.api.configuration.action.Proposable;
import org.talend.sdk.component.api.configuration.condition.ActiveIf;
import org.talend.sdk.component.api.configuration.ui.layout.GridLayout;
import org.talend.sdk.component.api.meta.Documentation;

@GridLayout({
        @GridLayout.Row({"VERSION", "dataset"}),
        @GridLayout.Row({"PAGE_CHOSED", "PAGE"}),
        @GridLayout.Row({"EDGE"}),
        @GridLayout.Row({"FIELDS"}),
        @GridLayout.Row({"METHOD"}),
        @GridLayout.Row({"CUSTOM_VALUES"}),
        @GridLayout.Row({"DIE_ON_ERROR"})
})
@GridLayout(names = GridLayout.FormType.ADVANCED, value = {
        @GridLayout.Row({"OUT_TO_FILE", "OUT_FILE_NAME"}),
        @GridLayout.Row({"TIMEOUT_SETTING", "CONNECT_TIMEOUT"})
})
@Documentation("FacebookInputCm Documentation")

@Data
public class InputCmMapperConfiguration implements Serializable {
    @Option
    @Documentation("")
    private FacebookInputcmDataSet dataset;

    @Option
    @Documentation("")
    @Proposable(FacebookConstants.VERSION_CHOSED)
    private String VERSION;


    @Option
    @Documentation("")
    @Proposable(FacebookConstants.PAGE_CHOSED)
    private String PAGE_CHOSED;

    @Option
    @Documentation("")
    @ActiveIf(target = "PAGE_CHOSED", value = {"ID"})
    private String PAGE;

    @Option
    @Documentation("")
    private String EDGE;

    @Option
    @Documentation("")
    private String FIELDS;

    @Option
    @Documentation("")
    @Proposable(FacebookConstants.METHOD_CHOSED)
    private String METHOD;

    @Option
    @Documentation("")
    private boolean OUT_TO_FILE;

    @Option
    @Documentation("")
    @ActiveIf(target = "OUT_TO_FILE", value = {"true"})
    private String OUT_FILE_NAME;

    @Option
    @Documentation("")
    private boolean TIMEOUT_SETTING;

    @Option
    @Documentation("")
    @ActiveIf(target = "TIMEOUT_SETTING", value = {"true"})
    private String CONNECT_TIMEOUT;

    @Option
    @Documentation("")
    private List<CUSTOM_VALUESConfiguration> CUSTOM_VALUES;

    @Option
    @Documentation("")
    private boolean DIE_ON_ERROR;


}