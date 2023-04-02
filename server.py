#	CSIMES Overview Server
#	Written by: JohnLesterDev :>

import os
from flask import Flask, render_template, redirect
from flaskext.markdown import Markdown


app = Flask(__name__, template_folder="server/md", static_folder="server/c")
Markdown(app)



@app.route('/overview')
def overview():
	return render_template("overview.md")
	
	
@app.route('/')
def default():
	return redirect("overview")
	
app.run(host="0.0.0.0", port=5555, debug=True)
