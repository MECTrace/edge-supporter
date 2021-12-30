#include "Cpu.h"

#define LED1        (1<<10)

void delay(volatile int cycles)
{
    while(cycles--);
}