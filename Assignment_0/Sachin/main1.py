!pip install pandas
!pip install numpy
!pip install matplotlib

import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from mpl_toolkits.mplot3d import Axes3D
%matplotlib inline

column_names = ['Width', 'Probability', 'AvgSuccessTime']
df = pd.read_csv("output.txt", header=None, names=column_names)


filtered_df = df[df['Width'] == 10]
# for const width =10
fixed_probability = filtered_df['Probability']
fixed_time = filtered_df["AvgSuccessTime"]

plt.figure(figsize=(6,4))
plt.plot(fixed_probability, fixed_time)
plt.xlabel("Probability")
plt.ylabel("Time")
plt.show()

filtered_df = df[df['Probability'] == 0.56]
# for const prob =0.56
fixed_Width = filtered_df['Width']
fixed_time_W = filtered_df["AvgSuccessTime"]

plt.figure(figsize=(6,4))
plt.plot(fixed_Width, fixed_time_W)
plt.xlabel("Width")
plt.ylabel("Time")
plt.show()

fig = plt.figure()
ax = fig.add_subplot(111, projection='3d')

# Plot the data
ax.scatter(df["Width"], df["Probability"], df['AvgSuccessTime'], )
