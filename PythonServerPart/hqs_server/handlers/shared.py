unity_sid = None
android_sid = None

def getAverage(data):
    # Remove the trailing comma, split by comma
    values = data.strip(',').split(',')

    # Convert to float and compute average
    values = [float(num) for num in values]
    average = sum(values) / len(values)

    return average