.data
a:
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
	sub %x3, %x3, %x3 // x3 <- 0
	sub %x4, %x4, %x4 // x4 <- 0
	load %x0, $n, %x8 // x8 <- 8
outerloop:
	blt %x3, %x8, innerloop // if x3 < x8, go to innerloop
	end // else end
	addi %x3, 1, %x4 //USELESS
innerloop:
	addi %x3, 1, %x4 // x4 <- x3 + 1
innerloopz:
	blt %x4, %x8, swap // if x4 < x8, go to swap
	addi %x3, 1, %x3 // x3 <- x3 + 1
	jmp outerloop // jumpback outerloop
swap:
	load %x3, $a, %x5 // x5 <- array[x3]
	load %x4, $a, %x6 // x6 <- array[x4]
	blt %x5, %x6, exchange //if x5 < x6, go to exchange
	addi %x4, 1, %x4 // x4 <- x4 + 1
	jmp innerloopz // jumpback innerloopz
exchange:
	sub %x7, %x7, %x7 // x7 <- 0
	add %x0, %x5, %x7 // x7 <- x5
	store %x6, 0, %x3 // array[x3] <- x6
	store %x7, 0, %x4 // array[x4] <- x7 (i. e, x5)
	addi %x4, 1, %x4 // x4 <- x4 + 1
	jmp innerloopz // jumpback innerloopz


0: 0
1: 0
2: 0
3: 0
4: 0, 1, 2
5: 0, 40
6: 0, 50 [30 done]
7: 0
8: 0, 8