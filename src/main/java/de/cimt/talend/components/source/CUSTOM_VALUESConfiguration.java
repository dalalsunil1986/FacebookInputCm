package de.cimt.talend.components.source;

import java.io.Serializable;

import lombok.Data;
import org.talend.sdk.component.api.configuration.Option;
import org.talend.sdk.component.api.configuration.ui.layout.GridLayout;
import org.talend.sdk.component.api.meta.Documentation;

@GridLayout({
        // the generated layout put one configuration entry per line,
        // customize it as much as needed
        @GridLayout.Row({"PROPERTY"}),
        @GridLayout.Row({"VALUE"})
})
@Documentation("")
@Data
public class CUSTOM_VALUESConfiguration implements Serializable {
    @Option
    @Documentation("")
    private String PROPERTY;

    @Option
    @Documentation("")
    private String VALUE;


}