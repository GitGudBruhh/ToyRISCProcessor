.data
n:
    10
    .text
main:
    load %x0, $n, %x2
    addi %x0, 1, %x1
    store %x1, 65522, %x0
    addi %x0, 65521, %x10
    addi %x0, 65522, %x11
    addi %x0, 65523, %x12
loop:
    beq $x2, %x0, end
    load %x10, 0, %x5
    load %x11, 0, %x6
    add %x5, %x6, %x7
    store %x7, 0, %x12
    addi %x10, 1, %x10
    addi %x11, 1, %x11
    addi %x12, 1, %x12
    subi %x2, 1, %x2
    jmp loop
end:
    end