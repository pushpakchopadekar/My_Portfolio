import { useEffect, useState } from "react";

// Point this to your deployed/local backend
const API_BASE = "https://myportfolio-production-c844.up.railway.app";

function authHeaders() {
  const token = localStorage.getItem("pf_admin_token");
  return token ? { Authorization: `Bearer ${token}` } : {};
}

async function api(path, options = {}) {
  const res = await fetch(`${API_BASE}${path}`, {
    ...options,
    headers: {
      "Content-Type": "application/json",
      ...authHeaders(),
      ...(options.headers || {}),
    },
  });
  if (res.status === 401) {
    localStorage.removeItem("pf_admin_token");
    window.location.reload();
    throw new Error("Unauthorized");
  }
  if (!res.ok) throw new Error(await res.text());
  const ct = res.headers.get("content-type") || "";
  return ct.includes("application/json") ? res.json() : res.text();
}

export default function AdminPanel() {
  const [token, setToken] = useState(localStorage.getItem("pf_admin_token"));
  if (!token) return <LoginScreen onLogin={(t) => { localStorage.setItem("pf_admin_token", t); setToken(t); }} />;
  return <Dashboard onLogout={() => { localStorage.removeItem("pf_admin_token"); setToken(null); }} />;
}

function LoginScreen({ onLogin }) {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const submit = async (e) => {
    e.preventDefault();
    setError(""); setLoading(true);
    try {
      const res = await fetch(`${API_BASE}/api/auth/login`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, password }),
      });
      if (!res.ok) throw new Error("Invalid username or password");
      const data = await res.json();
      onLogin(data.token);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="ap-root ap-center">
      <Style />
      <form className="ap-login-card" onSubmit={submit}>
        <div className="ap-logo"><span className="ap-logo-dot" />Admin Login</div>
        <p className="ap-muted" style={{ marginBottom: 24 }}>Manage your portfolio content</p>
        <input className="ap-input" placeholder="Username" value={username} onChange={(e) => setUsername(e.target.value)} />
        <input className="ap-input" placeholder="Password" type="password" value={password} onChange={(e) => setPassword(e.target.value)} />
        {error && <div className="ap-error">{error}</div>}
        <button className="ap-btn-primary" disabled={loading} style={{ width: "100%", marginTop: 8 }}>
          {loading ? "Signing in..." : "Sign In"}
        </button>
      </form>
    </div>
  );
}

const TABS = ["Profile", "Skills", "Experience", "Projects", "Certifications", "Resume", "Messages"];

function Dashboard({ onLogout }) {
  const [tab, setTab] = useState("Profile");
  return (
    <div className="ap-root">
      <Style />
      <div className="ap-shell">
        <aside className="ap-sidebar">
          <div className="ap-logo" style={{ marginBottom: 30 }}><span className="ap-logo-dot" />Portfolio Admin</div>
          {TABS.map((t) => (
            <div key={t} className={`ap-tab ${tab === t ? "active" : ""}`} onClick={() => setTab(t)}>{t}</div>
          ))}
          <div className="ap-tab ap-logout" onClick={onLogout}>Logout</div>
        </aside>
        <main className="ap-main">
          {tab === "Profile" && <ProfileTab />}
          {tab === "Skills" && <ListTab title="Skills" endpoint="/api/admin/skills" fields={[
            { key: "category", label: "Category" }, { key: "name", label: "Skill Name" }, { key: "sortOrder", label: "Sort Order", type: "number" },
          ]} />}
          {tab === "Experience" && <ListTab title="Experience" endpoint="/api/admin/experience" fields={[
            { key: "role", label: "Role" }, { key: "organization", label: "Organization" }, { key: "period", label: "Period" },
            { key: "description", label: "Description (one point per line)", textarea: true },
          ]} />}
          {tab === "Projects" && <ListTab title="Projects" endpoint="/api/admin/projects" fields={[
            { key: "title", label: "Title" }, { key: "description", label: "Description", textarea: true },
            { key: "tags", label: "Tags (comma separated)" }, { key: "githubUrl", label: "GitHub URL" },
            { key: "liveUrl", label: "Live URL" }, { key: "imageUrl", label: "Image URL" },
          ]} />}
          {tab === "Certifications" && <ListTab title="Certifications" endpoint="/api/admin/certifications" fields={[
            { key: "title", label: "Title" }, { key: "issuer", label: "Issuer" }, { key: "year", label: "Year" },
          ]} />}
          {tab === "Resume" && <ResumeTab />}
          {tab === "Messages" && <MessagesTab />}
        </main>
      </div>
    </div>
  );
}

function ProfileTab() {
  const [data, setData] = useState(null);
  const [saving, setSaving] = useState(false);
  const [saved, setSaved] = useState(false);
  const [photoFile, setPhotoFile] = useState(null);
  const [photoStatus, setPhotoStatus] = useState("");

  useEffect(() => { api("/api/admin/profile").then(setData); }, []);

  if (!data) return <div className="ap-muted">Loading...</div>;

  const basicFields = ["fullName", "tagline", "bio", "email", "phone", "githubUrl", "linkedinUrl"];
  const heroFields = [
    { key: "heroEyebrow", label: "Hero — small label above headline" },
    { key: "heroHeadline", label: "Hero — headline (use a new line for the highlighted last line)", textarea: true },
    { key: "heroSub", label: "Hero — subtext paragraph", textarea: true },
  ];
  const statFields = [
    { key: "statExperience", label: "Stat — Experience (e.g. \"6mo\")" },
    { key: "statCgpa", label: "Stat — CGPA (e.g. \"8.47\")" },
  ];

  const save = async () => {
    setSaving(true);
    await api("/api/admin/profile", { method: "PUT", body: JSON.stringify(data) });
    setSaving(false); setSaved(true);
    setTimeout(() => setSaved(false), 2000);
  };

  const uploadPhoto = async () => {
    if (!photoFile) return;
    setPhotoStatus("Uploading...");
    const form = new FormData();
    form.append("file", photoFile);
    const res = await fetch(`${API_BASE}/api/admin/photo`, {
      method: "POST",
      headers: { ...authHeaders() },
      body: form,
    });
    setPhotoStatus(res.ok ? "Photo updated ✓ (refresh the portfolio site to see it)" : "Upload failed");
  };

  return (
    <div>
      <h2 className="ap-h2">Profile</h2>

      <div className="ap-card" style={{ marginBottom: 20 }}>
        <div style={{ fontWeight: 600, marginBottom: 10 }}>Profile Photo</div>
        <p className="ap-muted" style={{ fontSize: 12, marginTop: 0 }}>Shown in the hero section on the portfolio.</p>
        <input className="ap-input" type="file" accept="image/*" onChange={(e) => setPhotoFile(e.target.files[0])} />
        <button className="ap-btn-primary" style={{ marginTop: 10 }} onClick={uploadPhoto}>Upload Photo</button>
        {photoStatus && <div className="ap-success" style={{ marginTop: 8 }}>{photoStatus}</div>}
      </div>

      <div className="ap-form-grid">
        {basicFields.map((f) => (
          <div key={f} className="ap-field">
            <label>{f}</label>
            {f === "bio" ? (
              <textarea className="ap-input" value={data[f] || ""} onChange={(e) => setData({ ...data, [f]: e.target.value })} />
            ) : (
              <input className="ap-input" value={data[f] || ""} onChange={(e) => setData({ ...data, [f]: e.target.value })} />
            )}
          </div>
        ))}
      </div>

      <div style={{ fontWeight: 600, margin: "24px 0 4px" }}>Hero Section Content</div>
      <div className="ap-form-grid">
        {heroFields.map((f) => (
          <div key={f.key} className="ap-field">
            <label>{f.label}</label>
            {f.textarea ? (
              <textarea className="ap-input" value={data[f.key] || ""} onChange={(e) => setData({ ...data, [f.key]: e.target.value })} />
            ) : (
              <input className="ap-input" value={data[f.key] || ""} onChange={(e) => setData({ ...data, [f.key]: e.target.value })} />
            )}
          </div>
        ))}
      </div>

      <div style={{ fontWeight: 600, margin: "24px 0 4px" }}>Hero Stats</div>
      <div className="ap-form-grid">
        {statFields.map((f) => (
          <div key={f.key} className="ap-field">
            <label>{f.label}</label>
            <input className="ap-input" value={data[f.key] || ""} onChange={(e) => setData({ ...data, [f.key]: e.target.value })} />
          </div>
        ))}
        <div className="ap-muted" style={{ fontSize: 12 }}>The "Projects" stat updates automatically from your Projects list — no need to set it here.</div>
      </div>

      <button className="ap-btn-primary" style={{ marginTop: 10 }} onClick={save} disabled={saving}>{saving ? "Saving..." : "Save Profile"}</button>
      {saved && <span className="ap-success">Saved ✓</span>}
    </div>
  );
}

function ListTab({ title, endpoint, fields }) {
  const [items, setItems] = useState([]);
  const [editing, setEditing] = useState(null); // object being edited, or null
  const [loading, setLoading] = useState(true);

  const load = () => api(endpoint).then((d) => { setItems(d); setLoading(false); });
  useEffect(() => { load(); }, [endpoint]);

  const startNew = () => {
    const blank = {}; fields.forEach((f) => (blank[f.key] = ""));
    setEditing(blank);
  };

  const save = async () => {
    const method = editing.id ? "PUT" : "POST";
    const url = editing.id ? `${endpoint}/${editing.id}` : endpoint;
    await api(url, { method, body: JSON.stringify(editing) });
    setEditing(null);
    load();
  };

  const remove = async (id) => {
    if (!confirm("Delete this item?")) return;
    await api(`${endpoint}/${id}`, { method: "DELETE" });
    load();
  };

  if (loading) return <div className="ap-muted">Loading...</div>;

  return (
    <div>
      <div className="ap-row-between">
        <h2 className="ap-h2">{title}</h2>
        <button className="ap-btn-primary" onClick={startNew}>+ Add New</button>
      </div>

      {editing && (
        <div className="ap-card" style={{ marginBottom: 20 }}>
          {fields.map((f) => (
            <div key={f.key} className="ap-field">
              <label>{f.label}</label>
              {f.textarea ? (
                <textarea className="ap-input" value={editing[f.key] || ""} onChange={(e) => setEditing({ ...editing, [f.key]: e.target.value })} />
              ) : (
                <input className="ap-input" type={f.type || "text"} value={editing[f.key] || ""} onChange={(e) => setEditing({ ...editing, [f.key]: e.target.value })} />
              )}
            </div>
          ))}
          <div style={{ display: "flex", gap: 10 }}>
            <button className="ap-btn-primary" onClick={save}>Save</button>
            <button className="ap-btn-ghost" onClick={() => setEditing(null)}>Cancel</button>
          </div>
        </div>
      )}

      <div className="ap-list">
        {items.map((it) => (
          <div key={it.id} className="ap-card ap-row-between">
            <div>
              <div style={{ fontWeight: 600 }}>{it[fields[0].key]}</div>
              <div className="ap-muted" style={{ fontSize: 12 }}>{it[fields[1] ? fields[1].key : fields[0].key]}</div>
            </div>
            <div style={{ display: "flex", gap: 8 }}>
              <button className="ap-btn-ghost" onClick={() => setEditing(it)}>Edit</button>
              <button className="ap-btn-danger" onClick={() => remove(it.id)}>Delete</button>
            </div>
          </div>
        ))}
        {items.length === 0 && <div className="ap-muted">No items yet — click "Add New" to create one.</div>}
      </div>
    </div>
  );
}

function ResumeTab() {
  const [file, setFile] = useState(null);
  const [status, setStatus] = useState("");

  const upload = async () => {
    if (!file) return;
    setStatus("Uploading...");
    const form = new FormData();
    form.append("file", file);
    const res = await fetch(`${API_BASE}/api/admin/resume`, {
      method: "POST",
      headers: { ...authHeaders() },
      body: form,
    });
    setStatus(res.ok ? "Resume updated ✓" : "Upload failed");
  };

  return (
    <div>
      <h2 className="ap-h2">Resume</h2>
      <p className="ap-muted">Upload a PDF to replace the file served by the "Download Resume" button on the portfolio.</p>
      <input className="ap-input" type="file" accept="application/pdf" onChange={(e) => setFile(e.target.files[0])} />
      <button className="ap-btn-primary" style={{ marginTop: 12 }} onClick={upload}>Upload</button>
      {status && <div className="ap-success" style={{ marginTop: 10 }}>{status}</div>}
    </div>
  );
}

function MessagesTab() {
  const [messages, setMessages] = useState([]);
  const load = () => api("/api/admin/messages").then(setMessages);
  useEffect(() => { load(); }, []);

  const remove = async (id) => {
    await api(`/api/admin/messages/${id}`, { method: "DELETE" });
    load();
  };

  return (
    <div>
      <h2 className="ap-h2">Contact Messages</h2>
      <div className="ap-list">
        {messages.map((m) => (
          <div key={m.id} className="ap-card">
            <div style={{ fontWeight: 600 }}>{m.name} — {m.email}</div>
            <div className="ap-muted" style={{ margin: "8px 0" }}>{m.message}</div>
            <button className="ap-btn-danger" onClick={() => remove(m.id)}>Delete</button>
          </div>
        ))}
        {messages.length === 0 && <div className="ap-muted">No messages yet.</div>}
      </div>
    </div>
  );
}

function Style() {
  return (
    <style>{`
      .ap-root { min-height: 100vh; background: #0A0118; color: #F3EFFF; font-family: 'Inter', system-ui, sans-serif; }
      .ap-center { display: flex; align-items: center; justify-content: center; }
      .ap-logo { font-weight: 700; font-size: 18px; display: flex; align-items: center; gap: 10px; }
      .ap-logo-dot { width: 12px; height: 12px; border-radius: 4px; background: linear-gradient(135deg,#7F5AF0,#2CB1FF,#FF5DA2); box-shadow: 0 0 16px rgba(127,90,240,0.7); }
      .ap-login-card { background: rgba(255,255,255,0.05); border: 1px solid rgba(255,255,255,0.12); border-radius: 18px; padding: 40px; width: 360px; backdrop-filter: blur(12px); }
      .ap-input { width: 100%; background: rgba(255,255,255,0.06); border: 1px solid rgba(255,255,255,0.15); border-radius: 10px; padding: 12px 14px; color: #F3EFFF; font-family: inherit; margin-bottom: 12px; outline: none; box-sizing: border-box; }
      .ap-input:focus { border-color: #7F5AF0; }
      textarea.ap-input { min-height: 90px; resize: vertical; }
      .ap-btn-primary { background: linear-gradient(135deg,#7F5AF0,#FF5DA2); border: none; color: white; padding: 12px 22px; border-radius: 10px; font-weight: 600; cursor: pointer; }
      .ap-btn-ghost { background: rgba(255,255,255,0.06); border: 1px solid rgba(255,255,255,0.15); color: #F3EFFF; padding: 10px 18px; border-radius: 10px; cursor: pointer; }
      .ap-btn-danger { background: rgba(255,70,70,0.15); border: 1px solid rgba(255,70,70,0.4); color: #ff9b9b; padding: 10px 18px; border-radius: 10px; cursor: pointer; }
      .ap-muted { color: #A79DC7; }
      .ap-error { color: #ff9b9b; font-size: 13px; margin-bottom: 10px; }
      .ap-success { color: #4ADE80; font-size: 13px; margin-left: 12px; }
      .ap-shell { display: flex; min-height: 100vh; }
      .ap-sidebar { width: 220px; padding: 26px 16px; border-right: 1px solid rgba(255,255,255,0.1); flex-shrink: 0; }
      .ap-tab { padding: 11px 14px; border-radius: 10px; cursor: pointer; font-size: 14px; color: #A79DC7; margin-bottom: 4px; }
      .ap-tab:hover { background: rgba(255,255,255,0.05); }
      .ap-tab.active { background: rgba(127,90,240,0.18); color: #F3EFFF; font-weight: 600; }
      .ap-logout { margin-top: 30px; color: #ff9b9b; }
      .ap-main { flex: 1; padding: 36px 40px; max-width: 760px; }
      .ap-h2 { font-size: 22px; margin: 0 0 20px; }
      .ap-row-between { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
      .ap-card { background: rgba(255,255,255,0.05); border: 1px solid rgba(255,255,255,0.12); border-radius: 14px; padding: 18px 20px; margin-bottom: 12px; }
      .ap-form-grid { display: grid; gap: 4px; margin-bottom: 20px; }
      .ap-field { margin-bottom: 4px; }
      .ap-field label { display: block; font-size: 12px; color: #A79DC7; margin-bottom: 6px; text-transform: capitalize; }
    `}</style>
  );
}
