import './App.css';
import {BrowserRouter, Route, Routes} from "react-router-dom";
import Header from "./components/layouts/Header";
import Footer from "./components/layouts/Footer";
import Layout from "./components/layouts/Layout";

function App() {
  return (
    <div className="App">

      <BrowserRouter>
        <Routes>
{/*           <Route path='/' element = { <MainComponent /> }> </Route>
           <Route path='/addLunch' element = { <AddLunch />}></Route>
           <Route path='/edit/:id' element = { <AddLunch /> }> </Route>*/}
        </Routes>

      </BrowserRouter>

    </div>
  );
}

export default App;
