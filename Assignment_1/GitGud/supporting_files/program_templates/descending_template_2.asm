	.data
a:
	70
	80
	40
	20
	10
	30
	50
	60
n:
	8
	.text
main:
	jmp outerloop
outerloop:
	subi %x3, 1, %x3
	beq %x3, %x0, exit
	load %x0, $n, %x4
	subi %x4, 1, %x4
	jmp innerloop
innerloop:
	beq %x4, %x0, outerloop
	subi %x4, 1 ,%x4
	load %x4, $a, %x5
	addi %x4, 1, %x7
	load %x7, $a, %x6
	bgt %x5, %x6, switch
	jmp innerloop
switch:
	load %x4, $a, %x6
	addi %x4, 1, %x7
	load %x7, $a, %x5
	store %x6 $a %x7
	store %x5 $a %x4
exit:
	end

