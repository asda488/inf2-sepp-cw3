package com.acmecorp;

import org.junit.jupiter.api.BeforeEach;
import static org.mockito.Mockito.mock;

public class SystemTestsBase {
    TextUserInterface textUserInterfaceMock;
    MenuController menuController;
    
    @BeforeEach
    void setUpTextInputAndMenuControllerAndDependencies(){
        //setup new mock text interface and attach to controller
        textUserInterfaceMock = mock(TextUserInterface.class);
        menuController = new MenuController(textUserInterfaceMock, new MockVerificationSystem());
    }
}
