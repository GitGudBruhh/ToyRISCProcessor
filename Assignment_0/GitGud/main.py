import subprocess

for border_width in range(10, 100, 10):
    for sensor_prob_percent_val in range(5, 25, 5):
        sensor_prob = sensor_prob_percent_val/100
        print(border_width, sensor_prob)
        subprocess.call(['java', 'Main', str(border_width), str(sensor_prob), 'output.txt'])
