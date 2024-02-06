	.data
a:
	10
	.text
main:
	load %x0 $a %x3
	divi %x3 %x4 2
	beq %x31 %x0 3
	addi %x0 1 %x10
	end
	subi %x0 1 %x10
	end
