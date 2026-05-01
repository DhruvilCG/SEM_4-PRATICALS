#include <stdio.h>

#define MAX 100

typedef struct {
    int pid;
    int arrival;
    int burst;
    int priority;
    int start;
    int completion;
    int waiting;
    int turnaround;
    int done;
} Process;

int main(void) {
    int n;
    Process p[MAX];

    printf("Enter number of processes: ");
    if (scanf("%d", &n) != 1 || n <= 0 || n > MAX) {
        printf("Invalid number of processes.\n");
        return 1;
    }

    for (int i = 0; i < n; i++) {
        p[i].pid = i + 1;
        p[i].done = 0;
        printf("Enter arrival time, burst time and priority for P%d: ", p[i].pid);
        if (scanf("%d %d %d", &p[i].arrival, &p[i].burst, &p[i].priority) != 3 || p[i].burst <= 0) {
            printf("Invalid input.\n");
            return 1;
        }
    }

    int completed = 0;
    int time = 0;

    while (completed < n) {
        int idx = -1;

        for (int i = 0; i < n; i++) {
            if (p[i].done || p[i].arrival > time) {
                continue;
            }

            if (idx == -1 || p[i].priority < p[idx].priority ||
                (p[i].priority == p[idx].priority && p[i].arrival < p[idx].arrival) ||
                (p[i].priority == p[idx].priority && p[i].arrival == p[idx].arrival && p[i].pid < p[idx].pid)) {
                idx = i;
            }
        }

        if (idx == -1) {
            time++;
            continue;
        }

        p[idx].start = time;
        p[idx].completion = time + p[idx].burst;
        p[idx].turnaround = p[idx].completion - p[idx].arrival;
        p[idx].waiting = p[idx].turnaround - p[idx].burst;
        p[idx].done = 1;

        time = p[idx].completion;
        completed++;
    }

    double totalWaiting = 0;
    double totalTurnaround = 0;

    printf("\nProcess\tAT\tBT\tPR\tCT\tTAT\tWT\n");
    for (int i = 0; i < n; i++) {
        totalWaiting += p[i].waiting;
        totalTurnaround += p[i].turnaround;
        printf("P%d\t%d\t%d\t%d\t%d\t%d\t%d\n",
               p[i].pid, p[i].arrival, p[i].burst, p[i].priority,
               p[i].completion, p[i].turnaround, p[i].waiting);
    }

    printf("\nAverage Waiting Time: %.2f", totalWaiting / n);
    printf("\nAverage Turnaround Time: %.2f\n", totalTurnaround / n);

    printf("\nGantt Chart:\n");
    time = 0;
    completed = 0;
    for (int i = 0; i < n; i++) {
        p[i].done = 0;
    }

    while (completed < n) {
        int idx = -1;

        for (int i = 0; i < n; i++) {
            if (p[i].done || p[i].arrival > time) {
                continue;
            }

            if (idx == -1 || p[i].priority < p[idx].priority ||
                (p[i].priority == p[idx].priority && p[i].arrival < p[idx].arrival) ||
                (p[i].priority == p[idx].priority && p[i].arrival == p[idx].arrival && p[i].pid < p[idx].pid)) {
                idx = i;
            }
        }

        if (idx == -1) {
            printf("| IDLE ");
            time++;
            continue;
        }

        printf("| P%d ", p[idx].pid);
        time += p[idx].burst;
        p[idx].done = 1;
        completed++;
    }
    printf("|\n");

    return 0;
}
