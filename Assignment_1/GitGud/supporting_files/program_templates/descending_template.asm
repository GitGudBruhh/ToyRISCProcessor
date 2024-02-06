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
	jmp %x0, outerloop

outerloop:
	//x3 = x3 - 1
	subi %x3, 1, %x3
	//if x3 == 0, branch to exit
	beq %x3, %x0, exit
	//load n to x4
	load %x0, $n, %x4
	//x4 = x4 - 1
	subi %x4, 1, %x4
	//branch to innerloop
	jmp %x0, innerloop

innerloop:
	//if x4 == 0, branch to outerloop
	beq %x4, %x0, outerloop
	//x4 = x4 - 1
	subi %x4, 1 ,%x4
	//load a[x4] to x5
	load %x4, $a, %x5
	//load a[x4+1] to x6
	addi %x4, 1, %x7
	load %x7, $a, %x6
	//if x5 > x6 branch to switch
	bgt %x5, %x6, switch
	//branch to innerloop
	jmp %x0, innerloop
switch:
	//load a[x4] to x6
	load %x4, $a, %x6
	//load a[x4+1] to x5
	addi %x4, 1, %x7
	load %x7, $a, %x5
	//store x6 to a[x4+1]
	store %x6 $a %x7
	//store x5 to a[x4]
	store %x5 $a %x4
exit:
	end

