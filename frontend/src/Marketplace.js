import React, { useEffect, useState } from 'react';
import API from './services/api';
import { Zap, Leaf, ShoppingCart, Plus, UserCircle, LogOut, Wallet, X, Edit3, Trash2 } from 'lucide-react';
import { useNavigate } from 'react-router-dom';

const Marketplace = () => {
    const [listings, setListings] = useState([]);
    const [stats, setStats] = useState({ totalListings: 0, totalEnergyKwh: 0 });
    const [walletBalance, setWalletBalance] = useState(parseFloat(localStorage.getItem('walletBalance')) || 1000.00);
    
    const [showModal, setShowModal] = useState(false);
    const [newListing, setNewListing] = useState({ energyKwh: '', pricePerUnit: '' });
    const [isSubmitting, setIsSubmitting] = useState(false);

    const navigate = useNavigate();
    const userRole = localStorage.getItem('userRole') || 'RESIDENT';
    const userName = localStorage.getItem('userName') || 'User';

    const loadData = async () => {
        try {
            const [listRes, statsRes] = await Promise.all([
                API.get('/listings/community'),
                API.get('/listings/stats')
            ]);
            setListings(listRes.data);
            setStats(statsRes.data);
        } catch (err) {
            console.error("Fetch error:", err);
        }
    };

    const handleAddListing = async (e) => {
        e.preventDefault();
        setIsSubmitting(true);
        try {
            await API.post('/auth/add-listing', {
                producerName: userName,
                energyKwh: newListing.energyKwh,
                pricePerUnit: newListing.pricePerUnit
            });
            alert("Success! Energy listing added.");
            setShowModal(false);
            setNewListing({ energyKwh: '', pricePerUnit: '' });
            loadData();
        } catch (err) {
            alert("Failed to add listing.");
        } finally {
            setIsSubmitting(false);
        }
    };

    const handleDelete = async (id) => {
        if (window.confirm("Are you sure you want to remove this listing?")) {
            try {
                await API.delete(`/auth/delete-listing/${id}`);
                alert("Listing deleted successfully.");
                loadData(); 
            } catch (err) {
                alert("Error deleting listing.");
            }
        }
    };

    const handleEdit = async (item) => {
        const newEnergy = prompt("Update Energy Amount (kWh):", item.energyKwh);
        const newPrice = prompt("Update Price per Unit (₹):", item.pricePerUnit);

        if (newEnergy && newPrice) {
            try {
                await API.put(`/auth/update-listing/${item.id}`, {
                    energyKwh: newEnergy,
                    pricePerUnit: newPrice
                });
                alert("Listing updated successfully!");
                loadData();
            } catch (err) {
                alert("Update failed.");
            }
        }
    };

    const handleLogout = () => {
        localStorage.clear();
        navigate('/login');
    };

    const handleRequest = async (id, producer, availableUnits, pricePerUnit) => {
        const qtyInput = document.getElementById(`qty-${id}`);
        const unitsToBuy = parseFloat(qtyInput.value);

        if (isNaN(unitsToBuy) || unitsToBuy <= 0) {
            alert("Enter a valid quantity.");
            return;
        }
        if (unitsToBuy > availableUnits) {
            alert(`Only ${availableUnits}kWh available.`);
            return;
        }

        const totalCost = unitsToBuy * pricePerUnit;
        if (walletBalance < totalCost) {
            alert("Insufficient Balance!");
            return;
        }

        if (window.confirm(`Confirm purchase: ₹${totalCost.toFixed(2)}?`)) {
            try {
                const res = await API.post('/auth/deduct-wallet', {
                    username: userName,
                    listingId: id,
                    amount: unitsToBuy,
                    totalCost: totalCost
                });
                
                // Update State and Storage
                setWalletBalance(res.data.newBalance);
                localStorage.setItem('walletBalance', res.data.newBalance);
                
                // ✅ UI Logic: Clear the input field after success
                qtyInput.value = ""; 

                loadData();
                alert("Transaction Successful!");
            } catch (err) {
                alert("Transaction failed.");
            }
        }
    };

    useEffect(() => { loadData(); }, []);

    return (
        <div className="min-h-screen bg-slate-50 font-sans text-slate-900">
            <nav className="sticky top-0 z-50 bg-white/70 backdrop-blur-md border-b border-slate-200 px-6 py-4">
                <div className="max-w-7xl mx-auto flex justify-between items-center">
                    <div className="flex items-center gap-2">
                        <div className="p-2 bg-emerald-500 rounded-lg text-white"><Leaf size={20} /></div>
                        <span className="text-xl font-bold tracking-tight text-emerald-900">GreenEnergy Hub</span>
                    </div>
                    <div className="flex items-center gap-4">
                        <div className="flex items-center gap-2 px-4 py-2 bg-amber-50 border border-amber-200 rounded-full shadow-sm">
                            <Wallet size={16} className="text-amber-600" />
                            <span className="text-sm font-bold text-amber-700">₹{walletBalance.toFixed(2)}</span>
                        </div>
                        <div className="hidden md:flex items-center gap-2 px-4 py-2 bg-white rounded-full border border-slate-200 shadow-sm">
                            <UserCircle size={18} className="text-emerald-600" />
                            <span className="text-sm font-semibold">{userName}</span>
                            <span className="text-[10px] bg-emerald-100 text-emerald-700 px-2 py-0.5 rounded-full font-bold uppercase tracking-wider">{userRole}</span>
                        </div>
                        <button onClick={handleLogout} className="p-2 text-slate-400 hover:text-rose-500 transition-colors">
                            <LogOut size={20} />
                        </button>
                    </div>
                </div>
            </nav>

            <main className="max-w-7xl mx-auto p-6 md:p-10">
                <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-8">
                    <div className="bg-gradient-to-br from-emerald-600 to-teal-700 rounded-[2.5rem] p-8 text-white shadow-xl">
                        <p className="text-emerald-100 font-medium mb-1 opacity-80">Total Community Energy</p>
                        <h2 className="text-5xl font-black">{stats.totalEnergyKwh} <span className="text-2xl font-light opacity-60">kWh</span></h2>
                    </div>
                    <div className="bg-white rounded-[2.5rem] p-8 border border-slate-200 shadow-sm">
                        <p className="text-slate-500 font-medium mb-1">Active P2P Listings</p>
                        <h2 className="text-5xl font-black text-slate-800">{stats.totalListings}</h2>
                    </div>
                </div>

                <div className="flex justify-between items-center mb-8">
                    <h3 className="text-2xl font-bold flex items-center gap-2"><Zap className="text-amber-500" size={24} /> Available Trades</h3>
                    {userRole === 'PRODUCER' && (
                        <button onClick={() => setShowModal(true)} className="flex items-center gap-2 bg-slate-900 text-white px-6 py-3 rounded-2xl font-bold hover:bg-emerald-600 shadow-lg transition-all">
                            <Plus size={20} /> New Listing
                        </button>
                    )}
                </div>

                <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-8">
                    {listings.map((item) => (
                        <div key={item.id} className="group bg-white rounded-[2.5rem] p-7 border border-slate-100 shadow-sm hover:shadow-2xl transition-all relative overflow-hidden">
                            {item.producerName === userName && (
                                <div className="absolute top-0 right-0 bg-emerald-500 text-white text-[8px] font-bold px-3 py-1 rounded-bl-xl uppercase tracking-widest">Your Listing</div>
                            )}

                            <div className="flex justify-between items-start mb-6">
                                <div>
                                    <span className="px-3 py-1 bg-emerald-50 text-emerald-700 text-[10px] font-black rounded-full border border-emerald-100 uppercase">SOLAR</span>
                                    <h4 className="text-xl font-bold text-slate-800 mt-1">{item.producerName}</h4>
                                </div>
                                <span className="text-emerald-600 font-black text-xl">₹{item.pricePerUnit}<span className="text-[10px] font-medium text-slate-400">/kWh</span></span>
                            </div>

                            <div className="mb-6">
                                <span className="block text-2xl font-black text-slate-800">{item.energyKwh}</span>
                                <span className="text-[10px] text-slate-400 font-bold uppercase tracking-widest">Available kWh</span>
                            </div>

                            <div className="space-y-3 pt-5 border-t border-slate-50">
                                <div className="flex items-center gap-2">
                                    <input type="number" id={`qty-${item.id}`} placeholder="Qty" className="flex-1 px-3 py-3 bg-slate-50 rounded-xl text-sm font-bold focus:ring-2 focus:ring-emerald-500 outline-none"/>
                                    <button onClick={() => handleRequest(item.id, item.producerName, item.energyKwh, item.pricePerUnit)} className="p-3 bg-emerald-500 text-white rounded-xl hover:bg-emerald-600 shadow-md">
                                        <ShoppingCart size={20} />
                                    </button>
                                </div>

                                {item.producerName === userName && (
                                    <div className="flex gap-2">
                                        <button onClick={() => handleEdit(item)} className="flex-1 flex justify-center items-center gap-1 py-2.5 bg-slate-100 text-slate-600 rounded-xl text-xs font-bold hover:bg-emerald-50 hover:text-emerald-600 transition-all">
                                            <Edit3 size={14} /> Edit
                                        </button>
                                        <button onClick={() => handleDelete(item.id)} className="flex-1 flex justify-center items-center gap-1 py-2.5 bg-rose-50 text-rose-600 rounded-xl text-xs font-bold hover:bg-rose-100 transition-all">
                                            <Trash2 size={14} /> Delete
                                        </button>
                                    </div>
                                )}
                            </div>
                        </div>
                    ))}
                </div>
            </main>

            {showModal && (
                <div className="fixed inset-0 z-[100] flex items-center justify-center p-4 bg-slate-900/60 backdrop-blur-sm">
                    <div className="bg-white rounded-[2.5rem] w-full max-w-md p-8 shadow-2xl relative">
                        <button onClick={() => setShowModal(false)} className="absolute right-6 top-6 text-slate-400 hover:text-rose-500"><X size={24} /></button>
                        <h2 className="text-2xl font-bold text-slate-900 mb-6">Create Energy Listing</h2>
                        <form onSubmit={handleAddListing} className="space-y-4">
                            <div>
                                <label className="block text-xs font-bold text-slate-400 uppercase mb-2 ml-1">Energy Amount (kWh)</label>
                                <input type="number" required className="w-full px-5 py-4 bg-slate-50 border-none rounded-2xl outline-none" placeholder="e.g. 50.0" value={newListing.energyKwh} onChange={(e) => setNewListing({...newListing, energyKwh: e.target.value})}/>
                            </div>
                            <div>
                                <label className="block text-xs font-bold text-slate-400 uppercase mb-2 ml-1">Price per kWh (₹)</label>
                                <input type="number" step="0.1" required className="w-full px-5 py-4 bg-slate-50 border-none rounded-2xl outline-none" placeholder="e.g. 6.5" value={newListing.pricePerUnit} onChange={(e) => setNewListing({...newListing, pricePerUnit: e.target.value})}/>
                            </div>
                            <button type="submit" disabled={isSubmitting} className="w-full py-4 bg-emerald-600 text-white rounded-2xl font-bold hover:bg-emerald-700 shadow-lg">
                                {isSubmitting ? "Adding..." : "Publish Listing"}
                            </button>
                        </form>
                    </div>
                </div>
            )}
        </div>
    );
};

export default Marketplace;