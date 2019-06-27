package edu.iis.mto.testreactor.exc4;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


public class DishWasherTest {



    @Mock
    DirtFilter dirtFilter;

    @Mock
    Door door;

    @Mock
    Engine engine;

    @Mock
    WaterPump waterPump;

    DishWasher dishWasher;
    ProgramConfiguration programConfiguration;
    ProgramConfiguration.Builder builderPC;
    RunResult runResult;
    RunResult.Builder builderRR;

    @Before
    public void init()
    {
        MockitoAnnotations.initMocks(this);
        builderPC = ProgramConfiguration.builder();
        builderPC.withProgram(WashingProgram.INTENSIVE);
        builderPC.withTabletsUsed(false);
        programConfiguration = builderPC.build();

        builderRR = RunResult.builder();
        builderRR.withRunMinutes(30);
        builderRR.withStatus(Status.SUCCESS);
        runResult = builderRR.build();

        dishWasher = new DishWasher(waterPump, engine, dirtFilter, door);

    }



    @Test
    public void itCompiles() {
        assertThat(true, Matchers.equalTo(true));
    }

    @Test
    public void testIfFunctionWithCorrectParameteresWillSuccess()
    {
        RunResult result = dishWasher.start(programConfiguration);
        Assert.assertThat(result.getStatus(), is(Status.SUCCESS));
    }

    @Test
    public void testIfDoorIsOpen()
    {
        when(door.closed()).thenReturn(false);
        RunResult result = dishWasher.start(programConfiguration);
        Assert.assertThat(result.getStatus(), is(Status.SUCCESS));
    }

    @Test
    public void testIfWaterPumpFunctionPourInvokedOnce() throws PumpException
    {
        dishWasher.start(programConfiguration);

        verify(waterPump, times(1)).pour(WashingProgram.INTENSIVE);
    }



    @Test
    public void testIfEngineFunctionRunProgramInvokedOnce() throws EngineException
    {

        dishWasher.start(programConfiguration);
        verify(engine, times(1)).runProgram(120);
    }


    @Test
    public void testIfDirtFilterHasNotEnoughtCapacity() throws Exception {
        builderPC.withTabletsUsed(true);
        programConfiguration = builderPC.build();
        when(dirtFilter.capacity()).thenReturn(30.0d);
        runResult = dishWasher.start(programConfiguration);
        assertEquals(Status.ERROR_FILTER, runResult.getStatus());
    }





}
