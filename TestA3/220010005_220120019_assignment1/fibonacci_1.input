	.data
n:
	15
	.text
main:
	load %x0, $n, %x1
    addi %x0, 65535, %x2
    addi %x0, 0, %x3
    addi %x0, 1, %x4
    store %x3, 0, %x2
    subi %x1, 1, %x1
    sub %x2, 1, %x2
    beq %x1, 0, terminate
    store %x4, 0, %x4
    subi %x1, 1, %x1
    sub %x2, 1, %x2
    beq %x1, 0, terminate
loop:
	add %x3, %x4, %x5
    store %x5, 0, %x5
    subi %x1, 1, %x1
    sub %x2, 1, %x2
    beq %x1, 0, terminate
    addi %x4, 0, %x3
    addi %x5, 0, %x4
	jmp loop
terminate:
	end