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
    PEX_RTOS_INIT();
    #endif

    delay(3600000);

    INT_SYS_InstallHandler(SS1_IRQn, SW_INT_1_isr, (isr_t*)0U);
    INT_SYS_DisableIRQ_MC_All(PIT_Ch2_IRQn);

     #ifdef PEX_RTOS_START
    PEX_RTOS_START();                
    #endif

    for(;;) {
    if(exit_code != 0) {
      break;
       }
  }
  return exit_code;
  

}