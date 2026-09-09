\# ResumeIQ



\## AI-Powered Resume \& Job Description Gap Analysis, ATS Scoring, and Resume Optimization



ResumeIQ is an AI-powered full-stack application designed to help job seekers understand how well their resume matches a specific job description and improve their resume accordingly.



The application analyzes a user's resume against a job description, identifies skill gaps, provides ATS-oriented feedback, and assists in generating an optimized resume.



\---



\## Features



\* Resume PDF/DOCX upload

\* Resume text extraction

\* Job description input

\* Resume vs. job description gap analysis

\* AI-powered ATS scoring

\* Missing skills identification

\* Resume improvement suggestions

\* User confirmation of possessed skills

\* AI-assisted resume optimization

\* JWT-based authentication

\* Google OAuth2 login

\* User-specific resume management

\* User-specific job description management



\---



\## Tech Stack



\### Frontend



\* React.js

\* Vite

\* JavaScript



\### Backend



\* Java

\* Spring Boot

\* Spring Security

\* Spring AI

\* Maven



\### Database



\* MySQL



\### AI



\* Ollama

\* Qwen 2.5 7B



\### Authentication



\* JWT

\* Google OAuth2 / OpenID Connect



\---



\## Project Structure



```text

ResumeIq/

│

├── resumeiq-backend/

│   ├── src/

│   ├── pom.xml

│   └── ...

│

├── resumeiq-frontend/

│   ├── src/

│   ├── package.json

│   └── ...

│

├── .gitignore

└── README.md

```



\---



\## Application Flow



```text

Resume Upload

&#x20;     ↓

Resume Text Extraction

&#x20;     ↓

Job Description Input

&#x20;     ↓

Resume + JD Analysis

&#x20;     ↓

AI Gap Analysis

&#x20;     ↓

ATS Score \& Suggestions

&#x20;     ↓

User Reviews Missing Skills

&#x20;     ↓

User Confirms Possessed Skills

&#x20;     ↓

AI Resume Optimization

```



\---



\## Authentication Flow



```text

&#x20;                   ┌─────────────────┐

&#x20;                   │      User       │

&#x20;                   └────────┬────────┘

&#x20;                            │

&#x20;                 ┌──────────┴──────────┐

&#x20;                 ↓                     ↓

&#x20;          Email/Password          Google Login

&#x20;                 │                     │

&#x20;                 └──────────┬──────────┘

&#x20;                            ↓

&#x20;                   Spring Security

&#x20;                            ↓

&#x20;                      JWT Token

&#x20;                            ↓

&#x20;                   React Application

```



\---



\## Backend



The backend is built using Spring Boot and provides REST APIs for authentication, resume management, job descriptions, analysis, and AI-powered resume processing.



Backend responsibilities include:



\* REST API development

\* Authentication and authorization

\* JWT security

\* Google OAuth2 authentication

\* Resume text extraction

\* Database interaction

\* AI integration

\* Resume/JD analysis



\---



\## Frontend



The frontend is built using React and Vite.



It provides the user interface for:



\* Authentication

\* Resume upload

\* Job description input

\* Gap analysis

\* ATS results

\* Resume optimization

\* User dashboard



\---



\## AI Integration



ResumeIQ uses Ollama locally with the Qwen 2.5 7B model for AI-powered resume analysis and optimization.



The AI layer is used for tasks such as:



\* Resume analysis

\* Job description analysis

\* Skill gap identification

\* ATS-oriented feedback

\* Resume improvement

\* Resume optimization



\---



\## Security



Sensitive credentials are not stored in the repository.



Environment variables are used for:



\* Database credentials

\* JWT secret

\* Google OAuth credentials

\* Other sensitive configuration



Actual secrets should never be committed to GitHub.



\---



\## Running the Project



\### Backend



Open the `resumeiq-backend` folder in IntelliJ IDEA and run the Spring Boot application.



The backend runs locally on:



```text

http://localhost:8080

```



\### Frontend



Open the `resumeiq-frontend` folder in VS Code.



Install dependencies:



```bash

npm install

```



Start the development server:



```bash

npm run dev

```



The frontend runs locally on:



```text

http://localhost:5173

```



\---



\## Project Status



ResumeIQ is currently under active development as a final-year engineering project.



\---



\## Future Improvements



\* Improved ATS scoring

\* Better resume optimization

\* More advanced job matching

\* Resume version management

\* Additional AI-powered career insights

\* Production deployment



