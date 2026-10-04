package com.example.model

data class Persona(
    val id: String,
    val name: String,
    val age: Int,
    val occupation: String,
    val bio: String,
    val fitnessLevel: String,
    val goal: String,
    val preferredDurationMinutes: Int,
    val avatarColorHex: Long,
    val initials: String,
    val hmwStatement: String
)

val AlexRivera = Persona(
    id = "alex",
    name = "Alex Rivera",
    age = 32,
    occupation = "Marketing Lead",
    bio = "Busy professional with unpredictable hours. Needs quick, adaptive workouts that fit chaotic schedules without feeling locked into rigid plans.",
    fitnessLevel = "Intermediate",
    goal = "High-efficiency HIIT & stress release",
    preferredDurationMinutes = 18,
    avatarColorHex = 0xFF0EA5E9,
    initials = "AR",
    hmwStatement = "How might we make workout plans adapt in real-time to sudden calendar shifts?"
)

val PriyaSingh = Persona(
    id = "priya",
    name = "Priya Singh",
    age = 27,
    occupation = "Elementary Teacher",
    bio = "Beginner embarking on her fitness journey. Seeks safe, encouraging social connections and non-intimidating accountability partners.",
    fitnessLevel = "Beginner",
    goal = "Consistency, gentle strength & positive habit building",
    preferredDurationMinutes = 20,
    avatarColorHex = 0xFF00B774,
    initials = "PS",
    hmwStatement = "How might we foster safe, positive social accountability without overwhelming beginners?"
)

val PredefinedPersonas = listOf(AlexRivera, PriyaSingh)
