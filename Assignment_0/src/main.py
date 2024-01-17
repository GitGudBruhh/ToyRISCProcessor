import subprocess
import numpy as np
import matplotlib.pyplot as plt
import csv

for border_width in range(10, 100, 10):
    for sensor_prob_percent_val in range(5, 25, 5):
        sensor_prob = sensor_prob_percent_val/100
        print(border_width, sensor_prob)
        subprocess.call(['javac', 'Main.java'])
        subprocess.call(['java', 'Main', str(border_width), str(sensor_prob), 'output.txt'])

width_list = []
prob_list = []
time_list = []

with open('output.txt', newline='') as csvfile:
    read_obj = csv.reader(csvfile, delimiter=',', quotechar='|')

    for row in read_obj:
        width_list.append(row[0])
        prob_list.append(row[1])
        time_list.append(row[2])

width_array = np.array(width_list)
prob_array = np.array(prob_list)
time_array = np.array(time_list)

# print(width_x, "\n", prob_array, "\n", time_array)

# filtered_df = df[df['Width'] == 10]
# # for const width =10
# fixed_probability = filtered_df['Probability']
# fixed_time = filtered_df["AvgSuccessTime"]
###############################################################################
f_width_prob_list = []
f_width_time_list = []

for i in range(len(width_array)):
    if(width_array[i] == 10):
        f_width_prob_list.append(prob_array[i])
        f_width_time_list.append(time_array[i])

f_width_prob_array = np.array(f_width_prob_list)
f_width_time_array = np.array(f_width_time_list)

plt.figure(figsize=(6,4))
plt.plot(f_width_prob_array, f_width_time_array)
plt.xlabel("Probability")
plt.ylabel("Time")
plt.show()

# filtered_df = df[df['Probability'] == 0.56]
# # for const prob =0.56
# fixed_Width = filtered_df['Width']
# fixed_time_W = filtered_df["AvgSuccessTime"]
###############################################################################
f_prob_width_list = []
f_prob_time_list = []

for i in range(len(prob_array)):
    if(prob_array[i] == 0.3):
        f_prob_width_list.append(width_array[i])
        f_prob_time_list.append(time_array[i])

f_prob_width_array = np.array(f_prob_width_list)
f_prob_time_array = np.array(f_prob_time_list)

plt.figure(figsize=(6,4))
plt.plot(f_prob_width_array, f_prob_time_array)
plt.xlabel("Width")
plt.ylabel("Time")
plt.show()

###############################################################################
fig = plt.figure()
ax = fig.add_subplot(111, projection='3d')
ax.plot(width_array, prob_array, time_array)
plt.show()
