import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import API from './services/api';
import {
  LogIn, UserPlus, ShieldCheck, Zap, Mail, User, Lock, Eye, EyeOff,
  Home, Sun, Loader2, CheckCircle2, AlertCircle, ArrowRight, Leaf
} from 'lucide-react';

const Login = () => {
  const [isRegister, setIsRegister] = useState(false);
  const [credentials, setCredentials] = useState({
    username: '',
    password: '',
    email: '',
    role: 'RESIDENT',
  });
  const [showPassword, setShowPassword] = useState(false);
  const [loading, setLoading] = useState(false);
  const [feedback, setFeedback] = useState(null); 
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setFeedback(null);
    setLoading(true);
    try {
      if (isRegister) {
        await API.post('/auth/signup', credentials);
        setFeedback({
          type: 'success',
          msg: 'Registration successful! Please sign in.',
        });
        setIsRegister(false);
      } else {
        const res = await API.post('/auth/login', {
          username: credentials.username,
          password: credentials.password,
        });

        // 🛡️ RBAC & Wallet Persistence Logic
        localStorage.setItem('userRole', res.data.role);
        localStorage.setItem('userName', res.data.username);
        
        // 💳 Backend se balance save kar rahe hain taaki Marketplace sahi dikhe
        if (res.data.walletBalance !== undefined) {
          localStorage.setItem('walletBalance', res.data.walletBalance);
        } else {
          localStorage.setItem('walletBalance', 1000.00); // Fallback
        }

        setFeedback({
          type: 'success',
          msg: `Welcome back, ${res.data.username}!`,
        });
        
        setTimeout(() => navigate('/marketplace'), 800);
      }
    } catch (err) {
      setFeedback({
        type: 'error',
        msg: isRegister
          ? 'Registration failed — username may already exist.'
          : 'Invalid credentials. Please try again.',
      });
    } finally {
      setLoading(false);
    }
  };

  const switchMode = (toRegister) => {
    setIsRegister(toRegister);
    setFeedback(null);
  };

  return (
    <div className="relative min-h-screen overflow-hidden bg-gradient-to-br from-emerald-50 via-white to-amber-50 flex items-center justify-center p-4 sm:p-6">
      <div className="pointer-events-none absolute -top-24 -left-24 h-96 w-96 rounded-full bg-emerald-300/40 blur-3xl" />
      <div className="pointer-events-none absolute -bottom-32 -right-24 h-[28rem] w-[28rem] rounded-full bg-amber-200/50 blur-3xl" />

      <div className="relative w-full max-w-md">
        <div className="flex justify-center mb-6">
          <div className="inline-flex items-center gap-2 rounded-full bg-white/70 backdrop-blur px-4 py-1.5 shadow-sm ring-1 ring-emerald-100">
            <span className="flex h-5 w-5 items-center justify-center rounded-full bg-gradient-to-br from-emerald-500 to-amber-400">
              <Zap className="h-3 w-3 text-white" strokeWidth={3} />
            </span>
            <span className="text-xs font-semibold tracking-wide text-emerald-800 uppercase">Energy Hub Patiala</span>
          </div>
        </div>

        <div className="relative rounded-[2rem] bg-white/80 backdrop-blur-xl ring-1 ring-white/60 shadow-2xl overflow-hidden">
          <div className="h-1.5 w-full bg-gradient-to-r from-emerald-500 via-teal-400 to-amber-400" />

          <div className="p-8 sm:p-10">
            <div className="text-center mb-7">
              <div className="inline-flex h-14 w-14 items-center justify-center rounded-2xl bg-emerald-600 text-white shadow-lg mb-4">
                {isRegister ? <UserPlus size={24} /> : <ShieldCheck size={24} />}
              </div>
              <h1 className="text-3xl font-bold text-slate-900">{isRegister ? 'Join Hub' : 'Welcome'}</h1>
              <p className="mt-2 text-sm text-slate-500">
                {isRegister ? 'Start trading green energy.' : 'Sign in to manage trades.'}
              </p>
            </div>

            <div className="relative grid grid-cols-2 rounded-2xl bg-slate-100 p-1 mb-6">
              <button type="button" onClick={() => switchMode(false)}
                className={`relative z-10 py-2.5 rounded-xl text-sm font-semibold transition-all ${!isRegister ? 'bg-white shadow text-emerald-700' : 'text-slate-500'}`}>
                Sign in
              </button>
              <button type="button" onClick={() => switchMode(true)}
                className={`relative z-10 py-2.5 rounded-xl text-sm font-semibold transition-all ${isRegister ? 'bg-white shadow text-emerald-700' : 'text-slate-500'}`}>
                Register
              </button>
            </div>

            {feedback && (
              <div className={`mb-5 flex items-center gap-2 rounded-xl px-4 py-3 text-sm border ${feedback.type === 'success' ? 'bg-emerald-50 text-emerald-800 border-emerald-200' : 'bg-rose-50 text-rose-800 border-rose-200'}`}>
                {feedback.type === 'success' ? <CheckCircle2 size={16} /> : <AlertCircle size={16} />}
                {feedback.msg}
              </div>
            )}

            <form onSubmit={handleSubmit} className="space-y-4">
              <div className="relative group">
                <User className="absolute left-4 top-1/2 -translate-y-1/2 h-4 w-4 text-slate-400 group-focus-within:text-emerald-600" />
                <input type="text" placeholder="Username" className="w-full pl-11 pr-4 py-3.5 rounded-2xl border border-slate-200 outline-none focus:border-emerald-500 focus:ring-4 focus:ring-emerald-500/10 transition-all"
                  onChange={(e) => setCredentials({ ...credentials, username: e.target.value })} required />
              </div>

              {isRegister && (
                <div className="relative group">
                  <Mail className="absolute left-4 top-1/2 -translate-y-1/2 h-4 w-4 text-slate-400" />
                  <input type="email" placeholder="Email" className="w-full pl-11 pr-4 py-3.5 rounded-2xl border border-slate-200 outline-none focus:border-emerald-500"
                    onChange={(e) => setCredentials({ ...credentials, email: e.target.value })} />
                </div>
              )}

              <div className="relative group">
                <Lock className="absolute left-4 top-1/2 -translate-y-1/2 h-4 w-4 text-slate-400" />
                <input type={showPassword ? 'text' : 'password'} placeholder="Password" className="w-full pl-11 pr-12 py-3.5 rounded-2xl border border-slate-200 outline-none focus:border-emerald-500"
                  onChange={(e) => setCredentials({ ...credentials, password: e.target.value })} required />
                <button type="button" onClick={() => setShowPassword(!showPassword)} className="absolute right-3 top-1/2 -translate-y-1/2 text-slate-400">
                  {showPassword ? <EyeOff size={16} /> : <Eye size={16} />}
                </button>
              </div>

              {isRegister && (
                <div className="grid grid-cols-2 gap-3 pt-2">
                  <button type="button" onClick={() => setCredentials({...credentials, role: 'RESIDENT'})}
                    className={`p-3 rounded-2xl border text-sm transition-all ${credentials.role === 'RESIDENT' ? 'bg-emerald-50 border-emerald-500 text-emerald-700' : 'bg-white'}`}>
                    <Home size={16} className="mb-1" /> <b>Resident</b><br/><small>Buy Energy</small>
                  </button>
                  <button type="button" onClick={() => setCredentials({...credentials, role: 'PRODUCER'})}
                    className={`p-3 rounded-2xl border text-sm transition-all ${credentials.role === 'PRODUCER' ? 'bg-emerald-50 border-emerald-500 text-emerald-700' : 'bg-white'}`}>
                    <Sun size={16} className="mb-1" /> <b>Producer</b><br/><small>Sell Energy</small>
                  </button>
                </div>
              )}

              <button type="submit" disabled={loading} className="w-full py-3.5 rounded-2xl bg-slate-900 text-white font-bold hover:bg-emerald-600 transition-all flex justify-center items-center gap-2">
                {loading ? <Loader2 className="animate-spin" size={18} /> : (isRegister ? 'Create Account' : 'Sign In')}
                {!loading && <ArrowRight size={18} />}
              </button>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Login;