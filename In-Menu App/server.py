from flask import Flask, request

app = Flask(__name__)

@app.route('/otp', methods=['POST'])
def receive_otp():
    otp = request.form.get('otp')
    print(f"[✅] OTP received: {otp}")
    return "Received", 200

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5050)
