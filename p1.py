import subprocess, yaml
raw = subprocess.check_output(["git", "show", "0474fd7:.github/workflows/gerar-apk.yml"]).decode()
s = [x["run"] for x in yaml.safe_load(raw)["jobs"]["build"]["steps"] if "gradle" in x.get("run","")][0]
s = s.replace("com.dosecerta.oficial", "com.dosecerta.app.final").replace("2.2", "2.5")
s = s.replace("import android.os.Build;", "import android.os.Build;\nimport android.widget.Toast;")
s = s.replace('in.putExtra("med_name", name);', 'in.putExtra("med_name", name); in.putExtra("med_id", m.optString("id","")); in.putExtra("time_str", t);')
s = s.replace('Bridge(Context c) { ctx = c; }', 'Bridge(Context c) { ctx = c; }\n@JavascriptInterface public String getConf() { SharedPreferences p = ctx.getSharedPreferences(PREFS_NAME, 0); String r = p.getString("cf", ""); if (!r.isEmpty()) p.edit().putString("cf", "").apply(); return r; }')
s = s.replace("document.getElementById('clock').textContent=now.toLocaleTimeString('pt-BR');", "document.getElementById('clock').textContent=now.toLocaleTimeString('pt-BR');if(window.AndroidBridge&&window.AndroidBridge.getConf){const c=window.AndroidBridge.getConf();if(c)c.split(';').filter(Boolean).forEach(x=>{const p=x.split('|');takeDose(Number(p[0]),p[1]);});}")
open("build_android.sh", "w").write(s)
