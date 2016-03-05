// Genetic5.cpp : Defines the entry point for the console application.
//

#pragma warning(disable:4786)		// disable debug warning

#include <iostream>					// for cout etc.
#include <vector>					// for vector class
#include <string>					// for string class
#include <algorithm>				// for sort algorithm
#include <time.h>					// for random seed
#include <math.h>					// for abs()

#define GA_POPSIZE        2048        // ga population size
#define GA_MAXITER        16384        // maximum iterations
#define GA_ELITRATE        0.10f        // elitism rate
#define GA_MUTATIONRATE    0.25f        // mutation rate
#define GA_MUTATION        RAND_MAX * GA_MUTATIONRATE
#define GA_TARGET        std::string("Hello world!")

using namespace std;                // polluting global namespace, but hey...

struct ga_struct {
    string str;                        // the string
    unsigned int fitness;            // its fitness
};

typedef vector<ga_struct> ga_vector;// for brevity

void init_population(ga_vector &population,
                     ga_vector &buffer) {
    int tsize = (int) GA_TARGET.size();

    for (int i = 0; i < GA_POPSIZE; i++) {
        ga_struct citizen;

        citizen.fitness = 0;
        citizen.str.erase();

        for (int j = 0; j < tsize; j++)
            citizen.str += (rand() % 90) + 32;

        population.push_back(citizen);
    }

    buffer.resize(GA_POPSIZE);
}

unsigned int heuristic1(string elem, string target) {
    unsigned int fitness = 0;
    for (int j = 0; j < target.size(); j++) {
        fitness += abs(int(elem[j] - target[j]));
    }
    return fitness;
}


unsigned int heuristic2(string elem, string target) {
    unsigned int fitness = 0;
    for (int j = 0; j < target.size(); j++) {
        fitness += elem[j] == target[j] ? 0 : 1;
    }
    return fitness;
}

bool strContains(string s, char c) {
    for (int i = 0; i < s.length(); i++) {
        if (s[i] == c) return true;
    }
    return false;
}

unsigned int generalHeuristic3(string elem, string target, unsigned int containsWeight, unsigned int eqWeight) {
    unsigned int fitness = 0;
    for (int j = 0; j < target.size(); j++) {
        bool contains = strContains(target, elem[j]);
        bool eq = elem[j] == target[j];

        fitness += (containsWeight + eqWeight) - containsWeight * contains - eqWeight * eq;
    }
    return fitness;
}

unsigned int heuristic3(string elem, string target) {
    return generalHeuristic3(elem, target, 1, 2);
}

void calc_fitness(ga_vector &population) {
    string target = GA_TARGET;
    for (int i = 0; i < GA_POPSIZE; i++) {
        population[i].fitness = heuristic3(population[i].str, target);
    }
}

bool fitness_sort(ga_struct x, ga_struct y) {
    return (x.fitness < y.fitness);
}

inline void sort_by_fitness(ga_vector &population) {
    sort(population.begin(), population.end(), fitness_sort);
}

void elitism(ga_vector &population,
             ga_vector &buffer, int esize) {
    for (int i = 0; i < esize; i++) {
        buffer[i].str = population[i].str;
        buffer[i].fitness = population[i].fitness;
    }
}

void mutate(ga_struct &member) {
    int tsize = (int) GA_TARGET.size();
    int ipos = rand() % tsize;
    int delta = (rand() % 90) + 32;

    member.str[ipos] = (char) ((member.str[ipos] + delta) % 122);
}

void mate(ga_vector &population, ga_vector &buffer) {
    int esize = (int) (GA_POPSIZE * GA_ELITRATE);
    size_t tsize = GA_TARGET.size();

    elitism(population, buffer, esize);

    // Mate the rest
    for (int i = esize; i < GA_POPSIZE; i++) {
        int i1 = rand() % (GA_POPSIZE / 2);
        int i2 = rand() % (GA_POPSIZE / 2);
        size_t spos = rand() % tsize;

        buffer[i].str = population[i1].str.substr(0, spos) +
                        population[i2].str.substr(spos, tsize - spos);

        if (rand() < GA_MUTATION) mutate(buffer[i]);
    }
}


inline void swap(ga_vector *&population,
                 ga_vector *&buffer) {
    ga_vector *temp = population;
    population = buffer;
    buffer = temp;
}

double average(ga_vector &vect) {
    double sum = 0;
    size_t size = vect.size();

    for (int i = 0; i < vect.size(); i++)
        sum += vect[i].fitness;

    return sum / size;
}

double variance(ga_vector &vect) {
    double sumOfDiffs = 0;
    double avg = average(vect);
    size_t size = vect.size();

    for (int i = 0; i < vect.size(); i++) {
        double diff = (double) vect[i].fitness - avg;
        sumOfDiffs += diff * diff;
    }

    return sumOfDiffs / size;
}

double standardDeviation(ga_vector &vect) {
    return sqrt(variance(vect));
}

void print_best(ga_vector &gav, unsigned int iteration) {
    cout << "Gen " << iteration << endl;
    cout << "Best: " << gav[0].str << " (" << gav[0].fitness << ")" << endl;
    cout << "Average Fitness: " << average(gav) << endl;
    cout << "Standard Deviation: " << standardDeviation(gav) << endl << endl;
}

int main() {
    srand(unsigned(time(NULL)));

    ga_vector pop_alpha, pop_beta;
    ga_vector *population, *buffer;

    init_population(pop_alpha, pop_beta);
    population = &pop_alpha;
    buffer = &pop_beta;

    for (unsigned int i = 0; i < GA_MAXITER; i++) {
        calc_fitness(*population);        // calculate fitness
        sort_by_fitness(*population);    // sort them
        print_best(*population, i);        // print the best one

        if ((*population)[0].fitness == 0) break;

        mate(*population, *buffer);        // mate the population together
        swap(population, buffer);        // swap buffers
    }

    return 0;
}
