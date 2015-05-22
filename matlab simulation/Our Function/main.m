%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% function point 2 experiment main script
% Nanjing University ICS
% Author: SSS,  Simon Chen
% Date: 2015/4/28
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%% step 1: set simulation datas
num_loops = 1000; % number of loops
num_node = 15;  % number of nodes


%% step 2: calculate total response time for every loop
% before executing the for loops,in order to satisfied the draw graphics 
% function, some variable should be idenditied
% firstly.
X_Axis = zeros(1, num_node + 1);  % xAxis of graphics, shows the number of monitors
Y_Axis = zeros(1, num_node + 1);  % YAxis of graphics, shows average response time under specific number of monitors

tic     % record the start time

for k=0:num_node    % the numbers of monitors from 0 to num_node
    num_monitor = k;    % number of monitors
    res_time_total_record = zeros(1, num_loops);    % record the total response time for each loop
    
    for i=1:num_loops       % the external for loops
        node_info = initilizeNodes(num_node);   % initilize the given nodes
        node_info = getRandomNum(node_info, num_monitor);   % change some values of nodes by numbers of monitors to the node's mu
        
        res_time_total = 0;     % total response time of this system in one for loop
        
        first_node_info = node_info(1:2, :);    % get the first two rows
        res_time_first = getFirstResTime(first_node_info);
        
        second_node_info = node_info(3: num_node - 2, :);   % total node list of second phase
        second_node_info_len = length(second_node_info(:, 1)); % the length of second_node_info
        second_node_info1 = second_node_info(1: 5, :); % first part
        second_node_info2 = second_node_info(6:8, :);  % second part
        second_node_info3 = second_node_info(9:11, :); % thrid part
        res_time_secnod = getSecondResTime(second_node_info1, second_node_info2, second_node_info3);
        
        third_node_info = node_info(num_node -1:num_node, :); % get the last two rows
        res_time_third = getThirdResTime(third_node_info);
        
        res_time_total = res_time_first + res_time_secnod + res_time_third;     % get res_time_first, res_time_secnod and res_time_third added up.
        res_time_total_record(1, i) = res_time_total;
    end
    res_time_avr = mean(res_time_total_record);
    X_Axis(1, k + 1) = k;
    Y_Axis(1, k + 1) = res_time_avr;
end

fprintf('Total executing time is:\n');
toc     % record the end time

%% step 3: draw graphics
drawGraphics(X_Axis, Y_Axis);