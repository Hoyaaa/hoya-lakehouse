from flask import Flask
from flask_cors import CORS
import subprocess
import sys
import os

app = Flask(__name__)
CORS(app)

@app.route("/run-todaymenu")
def run_todaymenu():
    try:
        script_path = os.path.join(os.path.dirname(__file__), "todaymenu_crawling.py")
        result = subprocess.run(
            [sys.executable, script_path], capture_output=True, text=True, check=True
        )
        return f"✅ 크롤링 성공!\n\n{result.stdout}", 200
    except subprocess.CalledProcessError as e:
        return f"❌ 오류 발생:\n\n{e.stderr}", 500

if __name__ == "__main__":
    print("🚀 Flask 서버 실행 중... http://localhost:5001")
    app.run(port=5001, debug=True)
