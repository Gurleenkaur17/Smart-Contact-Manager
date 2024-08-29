document.addEventListener('DOMContentLoaded', function () {
    const themeToggleButton = document.getElementById('theme_change_button');
    const currentTheme = localStorage.getItem('theme') || 'light';

    // Apply the theme
    document.documentElement.classList.add(currentTheme);

    // Toggle theme and store preference
    themeToggleButton.addEventListener('click', function () {
        if (document.documentElement.classList.contains('dark')) {
            document.documentElement.classList.remove('dark');
            document.documentElement.classList.add('light');
            localStorage.setItem('theme', 'light');
        } else {
            document.documentElement.classList.remove('light');
            document.documentElement.classList.add('dark');
            localStorage.setItem('theme', 'dark');
        }
    });

    
    document.documentElement.classList.add(localStorage.getItem('theme') || 'light');
});


// let currentTheme = "Light";
// const changeThemeButton = document.getElementById("theme_change_button");
// const mode = document.querySelector("html");
// const modeText = document.getElementById("modeText");
// changeThemeButton.addEventListener("click", changeTheme);

// function changeTheme(){
//     const theme = getTheme();    
//     mode.classList.remove(theme);

//     if(theme === "Light"){
//         setTheme("dark");
//         currentTheme = "dark";  
//         mode.classList.add(currentTheme);
//         modeText.innerText = "Dark"
//     }
//     else{
//         setTheme("Light");
//         currentTheme = "Light";
//         modeText.innerText = "Light"  
//     }
    
// }


// function setTheme(theme){
//     localStorage.setItem("theme", theme);
// }

// function getTheme(){
//     const theme = localStorage.getItem("theme");
//     return theme;
// }