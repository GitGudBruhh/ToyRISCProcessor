.data
array:
    40
	20
	50
	60
	80
	30
	10
	70
n:
    8
    .text
main:
    load %x0, $n, %x8
    addi %x0, 1, %x1
outerloop:
    subi %x8, 1, %x8
    beq %x8, 0, end
    load %x0, $n, %x9
    addi %x0, 0, %x2
    addi %x0, 1, %x3
innerloop:
    subi %x9, 1, %x9
    load %x2, array, %x15
    load %x3, array, %x16
    bgt %x15, %x16, jumpback
    store %x16, array, %x2
    store %x15, array, %x3
jumpback:
    beq %x9, 1, outerloop
    addi %x2, 1, %x2
    addi %x3, 1, %x3
    jmp innerloop
end:
    end