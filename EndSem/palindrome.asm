.data
n:
    6124216
    .text
main:
    addi %x0, 1, %x1
    load %x0, $n, %x5
    load %x0, $n, %x6
loop:
    divi %x6, 10, %x6
    add %x31, %x7, %x7
    beq %x6, 0, fin
    muli %x7, 10, %x7
    jmp loop
fin:
    beq %x5, %x7, end
    end
end:
    addi %x0, 1, %x30
    end