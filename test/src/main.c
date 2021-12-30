#include "Cpu.h"

#define LED1        (1<<10)

void delay(volatile int cycles)
{
    while(cycles--);
}

void PIT_Ch2_IRQHandler(void)
{
    PINS_DRV_TogglePins(LED7_PORT, LED7);       
    PIT_DRV_ClearStatusFlags(INST_PIT1, pit1_ChnConfig0.hwChannel);  
}

void SW_INT_1_isr(void)
{
     PINS_DRV_TogglePins(LED5_PORT, LED5); 
     INT_SYS_ClearSoftwareIRQRequest(SS1_IRQn);
}

int main(void){
    #ifdef PEX_RTOS_INIT

}